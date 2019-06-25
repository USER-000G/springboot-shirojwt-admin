package com.have.controller;

import com.have.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;


/**
 * @ClassName PermissionController
 * @Description TODO
 * @Author G
 * @Date 2019/5/29 17:47
 * @Version 1.0
 **/
@RestController
@Slf4j
public class PermissionController {
    @Autowired
    PermissionService permissionService;
}
