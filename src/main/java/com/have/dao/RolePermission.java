package com.have.dao;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @ClassName RolePermissionMapper
 * @Description TODO
 * @Author G
 * @Date 2019/5/29 9:28
 * @Version 1.0
 **/
@Data
@Component
public class RolePermission{

    private Long id;
    private Long role_id;
    private Long permission_id;

}
