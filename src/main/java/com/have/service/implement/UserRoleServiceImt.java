package com.have.service.implement;

import com.have.mapper.UserRoleMapper;
import com.have.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName UserRoleImt
 * @Description TODO
 * @Author G
 * @Date 2019/5/31 15:31
 * @Version 1.0
 **/
@Service
@Transactional
public class UserRoleServiceImt implements UserRoleService {
    @Autowired
    UserRoleMapper userRoleMapper;
}
