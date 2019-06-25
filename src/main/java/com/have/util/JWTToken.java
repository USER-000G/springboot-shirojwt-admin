package com.have.util;

import lombok.Data;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * @ClassName JWTToken
 * @Description TODO
 * @Author G
 * @Date 2019/6/12 14:57
 * @Version 1.0
 **/
@Data
public class JWTToken implements AuthenticationToken {

    private String token;

    public JWTToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

}
