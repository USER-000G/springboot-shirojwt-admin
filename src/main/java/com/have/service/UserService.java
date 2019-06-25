package com.have.service;

import com.have.dao.User;
import com.have.mapper.UserMapper;
import com.have.util.CustomException;
import com.have.util.Result;
import com.have.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName UserService
 * @Description TODO
 * @Author G
 * @Date 2019/5/24 15:10
 * @Version 1.0
 **/
public interface UserService {

    User findUserInfo(User user) throws Exception;

    int addUser(User user) throws Exception;

    int deleteUser(User user) throws Exception;

    int updateUser(User user) throws Exception;

}
