package com.have.util;

import com.have.util.Constants.ResultEnum;

/**
 * @ClassName ResultUtil
 * @Description TODO
 * @Author G
 * @Date 2019/5/27 12:51
 * @Version 1.0
 **/
public class ResultUtil {
    // 成功返回
    public static <T> Result<T> success(final int code, final String message, T data) {
        return new Result<>(code, message, data);
    }
    public static <T> Result<T> success(final String message, T data) {
        return new Result<>(ResultEnum.SUCCESS.getCode(), message, data);
    }
    public static <T> Result<T> success(final T data) {
        return new Result<>(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMessage(), data);
    }

    // 失败返回
    public static <T> Result<T> failure(final int code, final String message, T data) {
        return new Result<>(code, message, data);
    }
    public static <T> Result<T> failure(final String message) {
        return new Result<>(ResultEnum.FAIL.getCode(), message, null);
    }
    public static <T> Result<T> failure() {
        return new Result<>(ResultEnum.FAIL.getCode(), ResultEnum.FAIL.getMessage(), null);
    }

    // 异常响应
    public static <T> Result<T> error(final int code, final String message) {
        return new Result<>(code, message, null);
    }
    public static <T> Result<T> error(final String message) {
        return new Result<>(ResultEnum.SERVER_ERROR.getCode(), message, null);
    }
    public static <T> Result<T> error() {
        return new Result<>(ResultEnum.SERVER_ERROR.getCode(), ResultEnum.SERVER_ERROR.getMessage(), null);
    }

}
