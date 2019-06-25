package com.have.util.Constants;

import lombok.Getter;
import org.springframework.stereotype.Component;

/**
 * @ClassName Constant
 * @Description TODO
 * @Author G
 * @Date 2019/6/16 15:38
 * @Version 1.0
 **/
@Component
@Getter
public class Constant {
    /**
     * redis-OK
     */
    public final static String OK = "OK";

    /**
     * 验证码
     */
    public final static String DEFAULT_CAPTCHA_PARAM = "captcha";

    /**
     * redis token过期时间 30分钟
     */
    public final static int RefreshTokenExpireTime = 1800;

    /**
     * AES密码加密私钥
     */
    public final static String encryptAESKey = "V2FuZzkyNjQ1NGRTQkFQSUpXVA==";

    /**
     * 请求头key
     */
    public static final String REQUEST_AUTH_HEADER="Authorization";

    /**
     * redis-key-前缀-shiro:refresh_token:
     */
    public final static String PREFIX_SHIRO_REFRESH_TOKEN = "shiro:refresh_token:";

    /**
     * JWT-account:
     */
    public final static String ACCOUNT = "account";

    /**
     * JWT-currentTimeMillis:
     */
    public final static String CURRENT_TIME_MILLIS = "currentTimeMillis";
    /**
     * JWT-token-claim-EXPIRE_TIME: 过期时间5分钟
     */
    public final static int EXPIRE_TIME = 300;
    /**
     * JWT-encryptJWTKey:
     */
    public final static String encryptJWTKey = "U0JBUElKV1RkV2FuZzkyNjQ1NA==";
}
