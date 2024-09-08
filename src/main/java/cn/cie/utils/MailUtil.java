package cn.cie.utils;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 * Created by lh2 on 2023/6/2.
 * 邮件工具
 */
public class MailUtil {

    private final static String USERNAME = "rojeralone@163.com";
    private final static String PASSWORD = "password";           // 需要打开SMTP并使用此授权码登录

    /**
     * 发送邮件给指定人，需要主题和内容
     * @param user
     * @param title
     * @param content
     */
    public static void sendMail(String user, String title, String content) {
        SimpleEmail email = new SimpleEmail();
        email.setCharset("UTF8");
        email.setHostName("smtp.163.com");
        email.setAuthentication(USERNAME, PASSWORD);
        try {
            email.setFrom(USERNAME);
            email.addTo(user);
            email.setSubject(title);
            email.setMsg(content);
            email.send();
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送验证邮件给用户
     * @param user
     * @param code
     */
    public static void sendValidateMail(String user, String code) {

        String title = "WePlay注册验证码";
        String content = "感谢您注册WePlay，您的验证码为 \n" + code + "\n，请注意保存，此验证码有效期为 10分钟 ，并且只能使用一次。";

        sendMail(user, title, content);
    }

    /**
     * 忘记密码时发送验证邮件给用户
     * @param user
     * @param code
     */
    public static void sendFetchPwdMail(String user, String code) {

        String title = "WePlay找回密码";
        String content = "这封邮件是在验证您的WePlay邮箱，用来找回密码，如果不是您本人的操作，请忽略此邮件。\n您的验证码为 \n" + code + "\n请注意保存，此验证码有效期为 10分钟 ，并且只能使用一次。";

        sendMail(user, title, content);
    }

}
