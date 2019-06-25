package com.have.config;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.have.util.*;
import com.have.util.Constants.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.naming.AuthenticationException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.SignatureException;

/**
 * @ClassName JWTFilter
 * @Description TODO
 * @Author G
 * @Date 2019/6/12 15:05
 * @Version 1.0
 **/
@Slf4j
@Component
public class JWTFilter extends BasicHttpAuthenticationFilter {

    /**
     * 判断用户是否想要登入。
     * 检测header里面是否包含Authorization字段即可
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader(Constant.REQUEST_AUTH_HEADER);
        log.info("进入isLoginAttempt， 请求头的token: " + authorization);
        return authorization != null;
    }

    /**
     *
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader("Authorization");

        JWTToken token = new JWTToken(authorization);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        log.info("提交给realm进行登入, token" + token);
        // 在redis中看用户有没有退出登录 jwttoken失效
        String username = JWTUtil.getClaim(authorization, Constant.ACCOUNT);
        log.info("在redis中看用户有没有退出登录");
        if (JedisUtil.exists(Constant.PREFIX_SHIRO_REFRESH_TOKEN + username)) {
            getSubject(request, response).login(token);
            // 如果没有抛出异常则代表登入成功，返回true
            return true;
        }
        return false;
//        throw new CustomException("token失效, 请重新登录");
    }

    /**
     * 这里我们详细说明下为什么最终返回的都是true，即允许访问
     * 例如我们提供一个地址 GET /article
     * 登入用户和游客看到的内容是不同的
     * 如果在这里返回了false，请求会被直接拦截，用户看不到任何东西
     * 所以我们在这里返回true，Controller中可以通过 subject.isAuthenticated() 来判断用户是否登入
     * 如果有些资源只有登入用户才能访问，我们只需要在方法上面加上 @RequiresAuthentication 注解即可
     * 但是这样做有一个缺点，就是不能够对GET,POST等请求进行分别过滤鉴权(因为我们重写了官方的方法)，但实际上对应用影响不大
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginAttempt(request, response)) {
            try {
                executeLogin(request, response);
            } catch (Exception e) {
                String emsg = e.getMessage();
                log.error(emsg);
                Throwable throwable = e.getCause();
                if (throwable != null && throwable instanceof SignatureVerificationException) {
                    emsg = "token不正确(" + throwable.getMessage() + ")";
                } else if (throwable != null && throwable instanceof TokenExpiredException) {
                    // 该异常为 JWT token 已过期
                    if (this.refreshToken(request, response)) {
                        return true;
                    } else {
                        emsg = "token已过期(" + throwable.getMessage() + ")";
                    }
                } else {
                    if (throwable != null) {
                        emsg = throwable.getMessage();
                    }
                }
                this.badResponse(request, response, emsg);
                return false;
            }
        }
        return true;
    }

    /**
     * 刷新token
     */
    private boolean refreshToken(ServletRequest request, ServletResponse response) {
        String token = this.getAuthzHeader(request);
        String account = JWTUtil.getClaim(token, Constant.ACCOUNT);
        log.info("从token中获取用户查看redis token时间戳还在不在: " + account);
        log.info(new JedisUtil().toString()); // @Autowired jedisUtil  报错 null ???? jedis池为null
        if (JedisUtil.exists(Constant.PREFIX_SHIRO_REFRESH_TOKEN + account)) {
            String currentTimeMillisRedis = JedisUtil.getJson(Constant.PREFIX_SHIRO_REFRESH_TOKEN + account);
            log.info("看jwttoken 和 redis 存储信息是不是同一时间");
            log.info("jwttoken: " + JWTUtil.getClaim(token, Constant.CURRENT_TIME_MILLIS));
            log.info("jedisUtil: " + currentTimeMillisRedis);
            // 看jwttoken 和 redis 存储信息是不是同一时间
            if (JWTUtil.getClaim(token, Constant.CURRENT_TIME_MILLIS).equals(currentTimeMillisRedis)) {
                String currentTimeMillis = String.valueOf(System.currentTimeMillis());
                PropertiesUtil.readProperties("config.properties");
                String refreshTokenExpireTime = PropertiesUtil.getProperty("refreshTokenExpireTime");
                JedisUtil.setJson(Constant.PREFIX_SHIRO_REFRESH_TOKEN + account, currentTimeMillis, Integer.parseInt(refreshTokenExpireTime));
                token = JWTUtil.sign(account, currentTimeMillis);
                log.info("刷新token后 对比：");
                log.info("jwttoken: " + JWTUtil.getClaim(token, Constant.CURRENT_TIME_MILLIS));
                log.info("jedisUtil: " + JedisUtil.getJson(Constant.PREFIX_SHIRO_REFRESH_TOKEN + account));
                JWTToken jwtToken = new JWTToken(token);
                this.getSubject(request, response).login(jwtToken);
                // 前端每个请求都去更新token
                HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
                httpServletResponse.setHeader("Authorization", token);
                httpServletResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
                return true;
            }
        }
        return false;
    }


    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    private void badResponse(ServletRequest request, ServletResponse response, String emsg) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;charset=utf-8");
        PrintWriter out = null;
        try {
            out = httpServletResponse.getWriter();
            String data = JSONObject.toJSONString(new Result<>(HttpStatus.UNAUTHORIZED.value(), emsg, null));
            out.append(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }


}
