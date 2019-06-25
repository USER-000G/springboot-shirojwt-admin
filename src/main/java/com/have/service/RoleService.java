package com.have.service;

import com.have.dao.Role;
import com.have.dao.User;

import java.util.List;

/**
 * @ClassName RoleService
 * @Description TODO
 * @Author G
 * @Date 2019/5/28 20:00
 * @Version 1.0
 **/
public interface RoleService {
    List<Role> findRoleByUser(User user) throws Exception;
}
