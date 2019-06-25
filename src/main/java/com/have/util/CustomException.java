package com.have.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @ClassName CustomException
 * @Description TODO
 * @Author G
 * @Date 2019/5/28 10:03
 * @Version 1.0
 **/
@Data
@Builder
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private int eCode;
    private String eMessage;

    public CustomException(String s) {
    }
}
