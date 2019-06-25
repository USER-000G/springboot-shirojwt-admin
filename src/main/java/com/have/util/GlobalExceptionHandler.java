package com.have.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.aop.PermissionAnnotationHandler;
import org.apache.shiro.authz.permission.InvalidPermissionStringException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Set;

/**
 * @ClassName GlobalExceptionHandler
 * @Description TODO
 * @Author G
 * @Date 2019/5/28 9:40
 * @Version 1.0
 **/
@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler
    public Result unknownAccountError(UnknownAccountException e) {
        log.error("账号不存在", e);
        return ResultUtil.error("账号不存在");
    }
    @ExceptionHandler
    public Result incorrectCredentials(IncorrectCredentialsException e) {
        log.error("密码错误", e);
        return ResultUtil.error("账号或密码错误");
    }
    @ExceptionHandler
    public Result incorrectPermission(InvalidPermissionStringException e) {
        log.error("没有权限", e);
        return ResultUtil.error("没有权限");
    }
    @ExceptionHandler
    public Result incorrectAuthc(UnauthenticatedException e) {
        log.error(e.getMessage());
        return ResultUtil.error("未知身份访问");
    }
    @ExceptionHandler
    public Result incorrectAuthc(AuthenticationException e) {
        log.error(e.getMessage());
        return ResultUtil.error(e.getMessage());
    }
    @ExceptionHandler
    public Result unAuthorizedException(UnauthorizedException e) {
        log.error(e.getMessage());
        return ResultUtil.error("抱歉，你没有权限访问");
    }
    // 校验异常
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result validateException(ValidationException e) {
        if (e instanceof ConstraintViolationException) {
            ConstraintViolationException ve = (ConstraintViolationException) e;

            Set<ConstraintViolation<?>> violations = ve.getConstraintViolations();
            for (ConstraintViolation<?> item : violations) {
                System.out.println(item.getMessage());
            }

            String message = ve.getConstraintViolations().iterator().next().getMessage();
            return ResultUtil.error(message);
        }
        return ResultUtil.error("bad request");
    }

    @ExceptionHandler
    public Result methodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        return ResultUtil.error("参数不能为空");
    }

    @ExceptionHandler
    public Result unknownException(Exception e) {
        log.error("发生了未知异常 null ", e);
        return ResultUtil.error("系统异常");
    }
    @ExceptionHandler
    public Result customException(CustomException e) {
        log.error("自定义异常", e);
        return ResultUtil.error(e.getEMessage());
    }
}
