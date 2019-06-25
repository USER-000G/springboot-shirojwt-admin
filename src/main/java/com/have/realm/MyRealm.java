package com.have.realm;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.have.dao.Permission;
import com.have.dao.Role;
import com.have.dao.User;
import com.have.service.PermissionService;
import com.have.service.RoleService;
import com.have.service.UserService;
import com.have.util.Constants.Constant;
import com.have.util.JWTToken;
import com.have.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName MyRealm
 * @Description TODO
 * @Author G
 * @Date 2019/6/12 15:00
 * @Version 1.0
 **/
@Slf4j
@Service
public class MyRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private User user;
    @Autowired
    private User userParam;

    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("权限认证...");
        log.info(principals.toString());
        String username = jwtUtil.getClaim(principals.toString(), Constant.ACCOUNT);
        log.info("username: " + username);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        List<String> permissions = new ArrayList<String>();
        List<String> roles = new ArrayList<>();
        userParam.setUsername(username);
        try {
            log.info("根据用户名 从数据库查询角色赋给当前subject");
            List<Role> roleList = roleService.findRoleByUser(userParam);
            for (Role role : roleList) {
                roles.add(role.getRole_name());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        simpleAuthorizationInfo.addRoles(roles);
        log.info("角色赋予查询完成: " + roles.toString());
        try {
            log.info("根据用户名 从数据库查询权限赋给当前subject");
            List<Permission> permissionList = permissionService.findPermissionByUser(userParam);
            for (Permission permission : permissionList) {
                permissions.add(permission.getPerm_code());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        simpleAuthorizationInfo.addStringPermissions(permissions);
        log.info("权限赋予查询完成: " + permissions.toString());
        log.info("授权完成");
        return simpleAuthorizationInfo;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        log.info("进入 MyRealm doGetAuthenticationInfo ");
        // 解密获得username，用于和数据库进行对比
        String username = jwtUtil.getClaim(token, Constant.ACCOUNT);
        if (username == null) {
            log.info("解密jwttoken 获得token的用户名信息获取不到");
            throw new AuthenticationException("token无效");
        }
        log.info("解密jwttoken 获得token的用户名信息: " + username);

        try {
            userParam.setUsername(username);
            user = userService.findUserInfo(userParam);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        if (user == null) {
            log.info("解密jwttoken 后 获得username 根据用户名在数据库查询数据为空");
            throw new AuthenticationException("用户不存在");
        }
        log.info("解密jwttoken 获得token的用户名查询数据库获得用户信息: " + user.toString());

        if (!jwtUtil.verify(token, username)) {
            log.info("jwtUtil.verify token过期或无效");
            throw new TokenExpiredException("token过期或无效");
        }
        log.info("MyRealm 完成 token有效...");

        return new SimpleAuthenticationInfo(token, token, getName());
    }

}
