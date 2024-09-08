package cn.cie.event.handler;

import cn.cie.event.EventModel;
import cn.cie.event.EventType;
import cn.cie.utils.MailUtil;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lh2 on 2023/6/25.
 * 发送找回密码邮件事件
 */
@Service
public class SendFindPwdMailHandler implements EventHandler {

    public void doHandler(EventModel model) {
        MailUtil.sendFetchPwdMail(model.getExts("mail"), model.getExts("code"));
    }

    public List<EventType> getSupportEvent() {
        return Arrays.asList(EventType.SEND_FIND_PWD_EMAIL);
    }
}
