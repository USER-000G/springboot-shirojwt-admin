package com.have.mapper;

import com.github.pagehelper.Page;
import com.have.dao.Permission;
import com.have.dao.Role;
import com.have.dao.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * @ClassName PermissionMapper
 * @Description TODO
 * @Author G
 * @Date 2019/5/29 9:19
 * @Version 1.0
 **/
@Mapper
public interface PermissionMapper {

    List<Permission> findPermissionByRole(Role role);
    List<Permission> findPermissionByUser(User user);

}
