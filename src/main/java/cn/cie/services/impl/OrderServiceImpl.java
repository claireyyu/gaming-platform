package cn.cie.services.impl;

import cn.cie.entity.*;
import cn.cie.entity.dto.OrderDTO;
import cn.cie.entity.dto.OrderItemDTO;
import cn.cie.mapper.*;
import cn.cie.services.OrderService;
import cn.cie.utils.MsgCenter;
import cn.cie.utils.PageUtil;
import cn.cie.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by lh2 on 2023/6/12.
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderitemMapper orderitemMapper;
    @Autowired
    private GameMapper gameMapper;
    @Autowired
    private OrdermapperMapper ordermapperMapper;
    @Autowired
    private CodeMapper codeMapper;

    @Transactional
    public Result addOrders(int uid, List<Integer> gids) {
        Order order = new Order();
        order.setUid(uid);
        orderMapper.insert(order);
        double total = 0;
        // 对所有的游戏进行存储、计价
        for (Integer gid : gids) {
            Game game = gameMapper.selectById(gid);
            if (game.getStat() != Game.STAT_OK) {
                return Result.fail(MsgCenter.ERROR_PARAMS);
            }
            Orderitem item = new Orderitem();
            item.setGid(gid);
            // 如果现价不为空，那么就计算现价
            if (game.getDiscount() != null) {
                total += game.getDiscount();
                item.setPrice(game.getDiscount());
            } else {
                total += game.getPrice();
                item.setPrice(game.getPrice());
            }
            orderitemMapper.insert(item);
            Ordermapper mapper = new Ordermapper();
            mapper.setItem(item.getId());
            mapper.setOrder(order.getId());
            ordermapperMapper.insert(mapper);
        }
        // 更新所有游戏的价格
        order.setPrice(total);
        orderMapper.update(order);
        return Result.success(order);
    }

    public Result cancelOrder(int uid, int orderid) {
        Order order = orderMapper.selectById(orderid);
        // 如果orderid错误或者用户与订单不匹配，就返回错误
        if (order == null || order.getUid() != uid) {
            return Result.fail(MsgCenter.ERROR_PARAMS);
        }
        order.setStat(Order.STAT_CANCEL);
        if (1 == orderMapper.update(order)) {
            return Result.success();
        }
        return Result.fail(MsgCenter.ERROR);
    }

    @Transactional
    public Result pay(int uid, int orderid) {
        Order order = orderMapper.selectById(orderid);
        // 如果orderid错误或者用户与订单不匹配，就返回错误
        if (order == null || order.getUid() != uid) {
            return Result.fail(MsgCenter.ERROR_PARAMS);
        }
        order.setStat(Order.STAT_PAY);
        orderMapper.update(order);
        List<Integer> orderitems = ordermapperMapper.selectByOrder(orderid);
        List<Orderitem> orderitemList = orderitemMapper.selectByIds(orderitems);
        for (Orderitem item : orderitemList) {
            // 生产激活码并插入数据库
            String uuid = UUID.randomUUID().toString();
            Code code = new Code();
            code.setItem(item.getId());
            code.setUid(uid);
            code.setCode(uuid);
            codeMapper.insert(code);
            // 插入code后更新orderitem的对应codeid
            item.setCode(code.getId());
            orderitemMapper.update(item);
        }
        return Result.success();
    }

    public boolean exists(int orderid) {
        return orderMapper.selectById(orderid) != null;
    }


    public Result getNotPayOrders(int uid, int page) {
        PageUtil pageUtil = new PageUtil(orderMapper.getOrderNumsByUidAndStat(uid, Order.STAT_NOT_PAY), page);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("order", parseOrderByStatAndPage(uid, Order.STAT_NOT_PAY, pageUtil));
        map.put("page", pageUtil);
        return Result.success(map);
    }

    public Result getPaidOrders(int uid, int page) {
        PageUtil pageUtil = new PageUtil(orderMapper.getOrderNumsByUidAndStat(uid, Order.STAT_PAY), page);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("order", parseOrderByStatAndPage(uid, Order.STAT_PAY, pageUtil));
        map.put("page", pageUtil);
        return Result.success(map);
    }

    public Result getCancelOrders(int uid, int page) {
        PageUtil pageUtil = new PageUtil(orderMapper.getOrderNumsByUidAndStat(uid, Order.STAT_CANCEL), page);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("order", parseOrderByStatAndPage(uid, Order.STAT_CANCEL, pageUtil));
        map.put("page", pageUtil);
        return Result.success(map);
    }

    public void autoCancelOrder() {
        Date date = new Date();
        date.setTime(date.getTime() - 1000 * 60 * 15);  // 过期时间为15分钟
        orderMapper.updateStatByDate(Order.STAT_NOT_PAY, Order.STAT_CANCEL, date);
    }

    private List<OrderDTO> parseOrderByStatAndPage(Integer uid, Byte stat, PageUtil pageUtil) {
        List<OrderDTO> res = new ArrayList<OrderDTO>();
        List<Order> orders = orderMapper.selectByUidAndStatAndPage(uid, stat, pageUtil.getStartPos(), pageUtil.getSize());
        for (Order order : orders) {
            List<Integer> orderids = ordermapperMapper.selectByOrder(order.getId());    // 获取订单内所有的订单详情id
            List<Orderitem> orderitemList = orderitemMapper.selectByIds(orderids);      // 根据订单详情id获取订单详情
            List<OrderItemDTO> orderItemDTOList = new ArrayList<OrderItemDTO>();
            // 拼接订单详情信息，包括订单对应的游戏和激活码
            for (Orderitem orderitem : orderitemList) {
                Game game = gameMapper.selectById(orderitem.getGid());      // 游戏信息
                Code code;
                // 如果code不为空那么获取code
                if (orderitem.getCode() != null) {
                    code = codeMapper.selectById(orderitem.getCode());     // 激活码信息
                } else {
                    code = null;
                }
                orderItemDTOList.add(new OrderItemDTO(orderitem, game, code));
            }
            res.add(new OrderDTO(order, orderItemDTOList));
        }
        return res;
    }
}
