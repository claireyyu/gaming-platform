package cn.cie.exception;

import cn.cie.utils.MsgCenter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by lh2 on 2023/6/8.
 * 自定义异常
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super(MsgCenter.NOT_FOUND);
    }
}
