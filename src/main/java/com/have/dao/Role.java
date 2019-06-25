package com.have.dao;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * @ClassName Role
 * @Description TODO
 * @Author G
 * @Date 2019/5/28 19:33
 * @Version 1.0
 **/
@Data
@Component
public class Role {

    private Long id;
    private String role_name;

}
