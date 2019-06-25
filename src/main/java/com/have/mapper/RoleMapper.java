package com.have.mapper;

import com.have.dao.Role;
import com.have.dao.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName RoleMapper
 * @Description TODO
 * @Author G
 * @Date 2019/5/28 19:33
 * @Version 1.0
 **/
@Mapper
public interface RoleMapper {
    List<Role> findRoleByUser(User user);
}
