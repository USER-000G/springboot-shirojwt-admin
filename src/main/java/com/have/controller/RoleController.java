package com.have.controller;

import com.have.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName RoleController
 * @Description TODO
 * @Author G
 * @Date 2019/5/28 20:01
 * @Version 1.0
 **/
@RestController
@Slf4j
public class RoleController {

    @Autowired
    private RoleService roleService;

}
