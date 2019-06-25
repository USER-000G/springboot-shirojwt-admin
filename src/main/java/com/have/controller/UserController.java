package com.have.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.have.dao.Permission;
import com.have.dao.Role;
import com.have.dao.User;
import com.have.service.*;
import com.have.util.*;
import com.have.util.Constants.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisPool;

import javax.imageio.ImageIO;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName StartController
 * @Description TODO
 * @Author G
 * @Date 2019/5/24 14:21
 * @Version 1.0
 **/
@PropertySource(value = "classpath:config.properties", ignoreResourceNotFound = true)
@RestController
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @Autowired
    private DefaultKaptcha captchaProducer;

    @Value("${refreshTokenExpireTime}")
    private int refreshTokenExpireTime;

    @Autowired
    private User user;
    @Autowired
    private User userParam;
    @Autowired
    private Role paramRole;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private IDUtils idUtils;
    @Autowired
    private SnowUUid snowUUid;


    @GetMapping("/getCaptcha")
    public Map<String, String> getCaptcha() {
        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            String capText = captchaProducer.createText();
            String uuid = UUID.randomUUID().toString();
            JedisUtil.setJson(Constant.DEFAULT_CAPTCHA_PARAM, capText, 180);
            BufferedImage bi = captchaProducer.createImage(capText);
            ImageIO.write(bi, "png", baos);
            String imgBase64 = Base64.encodeBase64String(baos.toByteArray());
            Map<String, String> captchaMap = new HashMap<>();
            captchaMap.put(Constant.DEFAULT_CAPTCHA_PARAM,"data:image/jpeg;base64,"+imgBase64);
            log.info("redis里面的 验证码：" + JedisUtil.getJson(Constant.DEFAULT_CAPTCHA_PARAM));
            return captchaMap;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @PostMapping("/login")
    public Result login(@RequestBody JSONObject userObject, ServletResponse response) throws Exception {
        String currentTimeMillis = String.valueOf(System.currentTimeMillis());
        log.info("当前时间戳为：" + currentTimeMillis);
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String username = userObject.getString("username");
        String password = userObject.getString("password");
        String captcha = userObject.getString("captcha");
        // 图形验证码
        if (JedisUtil.exists(Constant.DEFAULT_CAPTCHA_PARAM)) {
            if (JedisUtil.getJson(Constant.DEFAULT_CAPTCHA_PARAM).equals(captcha)) {
                log.info("验证码有效");
            } else {
                log.info("验证码错误");
                return ResultUtil.failure("验证码错误");
            }
        } else {
            log.info("验证码失效");
            return ResultUtil.failure("验证码失效");
        }
        userParam.setUsername(username);
        user = userService.findUserInfo(userParam);
        log.info("前端传过来密码：" + password);
        try {
            log.info("数据库userinfo：" + user.toString());
        } catch (Exception e) {
        }
        if (user != null) {
                // 和数据库密码对比 已加密 非对称（前端密码先经过加密）公钥 （服务端 私钥）
                log.info("加密后的密码：" + AesCipherUtil.enCrypto(password));
                String aesPassword = AesCipherUtil.enCrypto(password);
                if (user.getPassword().equals(aesPassword)) {
                    Subject subject = SecurityUtils.getSubject();
                    // 同一时间戳下生成token
                    String token = JWTUtil.sign(username, currentTimeMillis);
                    // 存入redis 同一时间戳
                    JedisUtil.setJson(Constant.PREFIX_SHIRO_REFRESH_TOKEN + username, currentTimeMillis, refreshTokenExpireTime);
                    log.info("redis 中存储用户信息：" + JedisUtil.getJson(Constant.PREFIX_SHIRO_REFRESH_TOKEN + username));
                    log.info("redis存储时间为30分钟还剩多少时间：" + JedisUtil.ttl(Constant.PREFIX_SHIRO_REFRESH_TOKEN + username));
                    JWTToken jwtToken = new JWTToken(token);
                    log.info("生成jwtToken进行shiro登录: " + jwtToken.getToken());
                    subject.login(jwtToken);
                    Map<String , Object> map = new HashMap<String , Object>();
                    map.put("token", jwtToken);
                    // 写入请求头
                    httpServletResponse.setHeader(Constant.REQUEST_AUTH_HEADER, token);
                    httpServletResponse.setHeader("Access-Control-Expose-Headers", Constant.REQUEST_AUTH_HEADER);
                    log.info("jwttoken: " + JWTUtil.getClaim(token, Constant.CURRENT_TIME_MILLIS));
                    log.info("jedisUtil: " + JedisUtil.getJson(Constant.PREFIX_SHIRO_REFRESH_TOKEN + username));
                    log.info("登录完成...");
                    return ResultUtil.success("登录成功", jwtToken.getToken());
                } else {
                    return ResultUtil.failure("账号或密码错误");
                }
        } else {
            return ResultUtil.failure("用户不存在");
        }
    }

    @RequiresPermissions({"user:view", "user:edit"})
    @GetMapping("/getUserInfo")
    public Result getUserInfo (@RequestParam String username) throws Exception {
        userParam.setUsername(username);
        log.info("根据用户名或id查询用户信息 前端入参: " + userParam.toString());
        user = userService.findUserInfo(userParam);
        user.setPassword(AesCipherUtil.deCrypto(user.getPassword()));
        return ResultUtil.success(user);
    }

    // 新增用户
    @RequiresPermissions({"user:view", "user:edit"})
    @PostMapping("/addUser")
    public Result addUser(@RequestBody @Validated User user) throws Exception {
        // 密码加密存储
        String aesPassword = AesCipherUtil.enCrypto(user.getPassword());
        user.setPassword(aesPassword);
        // 生成唯一用户id
        Long userId = snowUUid.nextId();
        user.setId(userId);
        log.info("新增用户：" + user.toString());
        int result = userService.addUser(user);
        if (result > 0) {
            log.info("新增用户成功：" + result);
            return ResultUtil.success("新增操作成功", null);
        } else {
            log.info("新增用户失败：" + result);
            return ResultUtil.error("新增操作失败");
        }
    }
    // 删除用户
    @RequiresPermissions({"user:view", "user:edit"})
    @PostMapping("/deleteUser")
    public Result deleteUser(@RequestBody User user) throws Exception {
        int result = userService.deleteUser(user);
        if (result > 0) {
            log.info("删除用户成功：" + user.toString());
            return ResultUtil.success("删除操作成功", null);
        } else {
            log.info("删除用户失败：" + user.toString());
            return ResultUtil.error("删除操作失败");
        }
    }
    // 更新用户
    @RequiresPermissions({"user:view", "user:edit"})
    @PostMapping("/updateUser")
    public Result updateUser(@RequestBody User user) throws Exception {
        int result = userService.updateUser(user);
        if (result > 0) {
            log.info("更新用户成功: " + user.toString());
            return ResultUtil.success("更新操作成功", null);
        } else {
            log.info("更新用户失败: " + user.toString());
            return ResultUtil.error("更新操作失败");
        }
    }

    @RequiresPermissions("user:view")
    @GetMapping("/findRoleByUser")
    public Result findRoleByUser (@RequestBody JSONObject paramObject) throws Exception {
        userParam.setUsername(paramObject.getString("username"));
        userParam.setId(paramObject.getLong("id"));
        log.info("根据用户查询角色 前端入参: " + userParam);
        List<Role> roleInfo = roleService.findRoleByUser(userParam);
        return ResultUtil.success(roleInfo);
    }

    @RequiresPermissions("user:view")
    @GetMapping("/findPermissionByRole")
    public Result findPermissionByRole (@RequestBody JSONObject paramObject) throws Exception {
        paramRole.setRole_name(paramObject.getString("roleName"));
        log.info("根据角色查询权限 前端入参: " + paramRole.toString());
        List<Permission> permissionList = permissionService.findPermissionByRole(paramRole);
        return ResultUtil.success(permissionList);
    }

    @RequiresPermissions("user:view")
    @GetMapping("/findPermissionByUser")
    public Result findPermissionByUser (@RequestBody JSONObject paramObject) throws Exception {
        userParam.setUsername(paramObject.getString("username"));
        userParam.setId(paramObject.getLong("id"));
        log.info("根据用户查询权限 前端入参: " + userParam.toString());
        // 分页
        PageHelper.startPage(2, 2);
        List<Permission> permissionList = permissionService.findPermissionByUser(userParam);
        PageInfo<Permission> selectPageData = new PageInfo<>(permissionList);
        return ResultUtil.success(selectPageData);
    }

}
