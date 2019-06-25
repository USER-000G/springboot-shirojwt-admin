package com.have.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.have.util.Constants.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * @ClassName JWTUtil
 * @Description TODO
 * @Author G
 * @Date 2019/6/12 14:40
 * @Version 1.0
 **/
@Component
@Slf4j
public class JWTUtil {

    /**
     * 校验token是否正确
     * @param token 密钥
     * @param username 用户名
     * @return 是否正确
     */
    public static boolean verify(String token, String username) {
        try {
            String secret = username + Base64ConvertUtil.encode(Constant.encryptJWTKey);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     * @return token中包含的用户名
     */
    public static String getClaim(String token, String claim) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(claim).asString();
        } catch (JWTDecodeException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * 生成签名,5min后过期
     * @param username 用户名
     * @return 加密的token
     */
    public static String sign(String username, String currentTime) {
        try {
            // 过期时间毫秒单位
            Date date = new Date(System.currentTimeMillis()+Constant.EXPIRE_TIME * 1000);
//            String secret = username + password + Base64ConvertUtil.decode(encryptJWTKey);
            String secret = username + Base64ConvertUtil.encode(Constant.encryptJWTKey);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // 附带username信息
            return JWT.create()
                    .withClaim(Constant.ACCOUNT, username)
                    .withClaim(Constant.CURRENT_TIME_MILLIS, currentTime)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
            return null;
        }
    }

}
