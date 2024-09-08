package cn.cie.services;

import cn.cie.entity.Kind;
import cn.cie.entity.dto.GameDTO;
import cn.cie.utils.Result;

import java.util.List;

/**
 * Created by lh2 on 2023/6/6.
 */
public interface KindService {

    /**
     * 根据种类id获取种类名字，如果不存在返回null
     * @param id
     * @return
     */
    String getNameById(Integer id);

    /**
     * 获取所有的种类
     * @return
     */
    Result<Kind> getAll();

    /**
     * 根据种类获取所有的游戏，包括游戏信息和游戏的种类、标签
     * @param kind
     * @param page
     * @return
     */
    Result<List<GameDTO>> getGamesByKind(int kind, int page);

}
