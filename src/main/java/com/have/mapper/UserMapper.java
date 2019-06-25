package com.have.mapper;

import com.have.dao.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.mapper.MapperFactoryBean;

import java.util.List;

/**
 * @ClassName UserMapper
 * @Description TODO
 * @Author G
 * @Date 2019/5/24 16:36
 * @Version 1.0
 **/
@Mapper
public interface UserMapper {

    User findUserInfo(User user);

    int addUser(User user);

    int deleteUser(User user);

    int updateUser(User user);

}
