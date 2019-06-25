package com.have.config;

import com.alibaba.fastjson.JSON;
import com.have.util.Constants.Constant;
import com.have.util.Constants.ResultEnum;
import com.have.util.JWTToken;
import com.have.util.JWTUtil;
import com.have.util.JedisUtil;
import com.have.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @ClassName SystemLogoutFilter
 * @Description TODO
 * @Author G
 * @Date 2019/6/17 10:43
 * @Version 1.0
 **/
@Slf4j
public class SystemLogoutFilter extends LogoutFilter {

    // 用拦截器处理退出操作
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        log.info("进入logout filter...");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader(Constant.REQUEST_AUTH_HEADER);
        String account = JWTUtil.getClaim(token, Constant.ACCOUNT);

        Subject subject = getSubject(request, response);
        try {
            subject.logout();
            // 清除 jwttoken redis
            JedisUtil.delKey(Constant.PREFIX_SHIRO_REFRESH_TOKEN + account);
            log.info("清楚redis 用户登录时间戳信息");
        } catch (Exception ex) {
            log.error("退出登录错误",ex);
        }
        this.writeResult(response);
        //不执行后续的过滤器
        return false;
    }

    private void writeResult(ServletResponse response){
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;charset=utf-8");
        //响应Json结果
        PrintWriter out = null;
        try {
            out = response.getWriter();
            Result result = new Result<>(ResultEnum.SUCCESS.getCode(), "退出成功", null);
            out.append(JSON.toJSONString(result));
        } catch (IOException e) {
            log.error("返回Response信息出现IOException异常:" + e.getMessage());
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
