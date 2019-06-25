package com.have.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * @ClassName Base64ConvertUtil
 * @Description TODO
 * @Author G
 * @Date 2019/6/16 14:08
 * @Version 1.0
 **/
public class Base64ConvertUtil {
    /**
     * 加密
     * @param str
     * @return java.lang.String
     */
    public static String encode(String str) throws UnsupportedEncodingException {
        byte[] encodeBytes = Base64.getEncoder().encode(str.getBytes("utf-8"));
        return new String(encodeBytes);
    }

    /**
     * 解密
     * @param str
     * @return java.lang.String
     */
    public static String decode(String str) throws UnsupportedEncodingException {
        byte[] decodeBytes = Base64.getDecoder().decode(str.getBytes("utf-8"));
        return new String(decodeBytes);
    }
}
