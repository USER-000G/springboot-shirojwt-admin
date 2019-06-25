package com.have.service;

import com.have.dao.Permission;
import com.have.dao.Role;
import com.have.dao.User;

import java.util.List;


/**
 * @ClassName PermissionService
 * @Description TODO
 * @Author G
 * @Date 2019/5/29 18:00
 * @Version 1.0
 **/
public interface PermissionService {

    List<Permission> findPermissionByRole(Role role) throws Exception;
    List<Permission> findPermissionByUser(User user) throws Exception;

}
