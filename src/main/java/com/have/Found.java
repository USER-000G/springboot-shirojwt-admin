package com.have;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.servlet.KaptchaServlet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @ClassName Found
 * @Description TODO
 * @Author G
 * @Date 2019/5/24 14:08
 * @Version 1.0
 **/
@SpringBootApplication
@EnableTransactionManagement
@Slf4j
public class Found {

    public static void main(String[] args) {
        log.info("启动项目...");
        ConfigurableApplicationContext context=SpringApplication.run(Found.class, args);
        String port=context.getEnvironment().getProperty("server.port");
        System.out.println("port: " + port);
    }

}
