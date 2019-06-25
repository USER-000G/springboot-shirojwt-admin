package com.have.util;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @ClassName IDUtils
 * @Description TODO
 * @Author G
 * @Date 2019/6/25 9:29
 * @Version 1.0
 **/
@Component
public class IDUtils {
    // 以当前时间作为前缀，uuid为后缀
    public String getID(){
        String transactionId = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())
                + UUID.randomUUID();
        return transactionId;
    }
}
