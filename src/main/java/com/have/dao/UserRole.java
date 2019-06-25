package com.have.dao;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @ClassName UserRole
 * @Description TODO
 * @Author G
 * @Date 2019/5/29 9:53
 * @Version 1.0
 **/
@Data
@Component
public class UserRole {

    private Long id;
    private Long user_id;
    private Long role_id;

}
