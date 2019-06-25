package com.have.service.implement;

import com.have.dao.Permission;
import com.have.dao.Role;
import com.have.dao.User;
import com.have.mapper.PermissionMapper;
import com.have.service.PermissionService;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @ClassName PermissionServiceImt
 * @Description TODO
 * @Author G
 * @Date 2019/5/29 18:01
 * @Version 1.0
 **/
@Service
@Transactional
public class PermissionServiceImt implements PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public List<Permission> findPermissionByRole (Role role) {
        return permissionMapper.findPermissionByRole(role);
    }

    @Override
    public List<Permission> findPermissionByUser (User user) {
        return permissionMapper.findPermissionByUser(user);
    }
}
