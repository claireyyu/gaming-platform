package cn.cie.utils;

import org.springframework.util.DigestUtils;

/**
 * Created by lh2 on 2023/6/6.
 * 将密码进行MD5加密
 */
public class PasswordUtil {

    private static final String SALT = "gp30#?>2a";

    public static String pwd2Md5(String paswword) {
        return DigestUtils.md5DigestAsHex((paswword + SALT).getBytes());
    }

}
