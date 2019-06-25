package com.have.service.implement;

import com.have.dao.Role;
import com.have.dao.User;
import com.have.mapper.RoleMapper;
import com.have.mapper.UserMapper;
import com.have.service.UserService;
import com.have.util.CustomException;
import com.have.util.Result;
import com.have.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;

import java.sql.SQLException;
import java.util.List;

/**
 * @ClassName
 * @Description TODO
 * @Author G
 * @Date 2019/5/29 11:42
 * @Version 1.0
 **/
@Service
@Slf4j
@Transactional
public class UserServiceImt<T> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findUserInfo (User user){
        return userMapper.findUserInfo(user);
    }

    @Override
    public int addUser (User user) {
        return userMapper.addUser(user);
    }

    @Override
    public int deleteUser (User user) {
        return userMapper.deleteUser(user);
    }

    @Override
    public int updateUser (User user) {
        return userMapper.updateUser(user);
    }

}
