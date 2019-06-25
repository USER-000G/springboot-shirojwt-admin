package com.have.dao;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * @ClassName UserDto
 * @Description TODO
 * @Author G
 * @Date 2019/6/21 8:20
 * @Version 1.0
 **/
@Data
@Table(name = "user")
class UserDto extends User {
    @Transient
    private List<Role> roles;
    private List<Permission> permissions;
}
