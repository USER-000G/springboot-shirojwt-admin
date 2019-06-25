package com.have.dao;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @ClassName Permission
 * @Description TODO
 * @Author G
 * @Date 2019/5/29 9:19
 * @Version 1.0
 **/
@Data
@Component
public class Permission {

    private Long id;
    private String perm_name;
    private String perm_code;

}
