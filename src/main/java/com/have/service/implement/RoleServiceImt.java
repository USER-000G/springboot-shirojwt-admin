package com.have.service.implement;

import com.have.dao.Role;
import com.have.dao.User;
import com.have.mapper.RoleMapper;
import com.have.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName RoleServiceImt
 * @Description TODO
 * @Author G
 * @Date 2019/5/28 20:00
 * @Version 1.0
 **/
@Service
@Transactional
public class RoleServiceImt implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> findRoleByUser(User user) {
        return roleMapper.findRoleByUser(user);
    }

}
