package com.have.util.Constants;

import lombok.Getter;

/**
 * @ClassName ResultEnum
 * @Description TODO
 * @Author G
 * @Date 2019/5/27 14:32
 * @Version 1.0
 **/
@Getter
public enum ResultEnum {
    SUCCESS(0, "成功"),
    FAIL(1, "失败"),
    NOT_FOUND(404, "不存在"),
    SERVER_ERROR(500, "服务异常");

    private int code;
    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
