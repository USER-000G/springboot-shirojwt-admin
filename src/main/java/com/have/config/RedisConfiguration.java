package com.have.config;

import com.have.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


/**
 * @ClassName RedisConfiguration
 * @Description TODO
 * @Author G
 * @Date 2019/5/30 10:52
 * @Version 1.0
 **/
@Configuration
@EnableCaching
@Slf4j
@PropertySource("classpath:redis.properties")
public class RedisConfiguration extends CachingConfigurerSupport {

    @Value("${spring.redis.host}")
    private String host = "127.0.0.1";

    @Value("${spring.redis.port}")
    private int port = 6379;

    @Value("${spring.redis.timeout}")
    private int timeout = 10000;

    @Value("${spring.redis.pool.max-idle}")
    private int maxIdle = 8;

    @Value("${spring.redis.pool.max-wait}")
    private long maxWaitMillis = -1;

    @Value("${spring.redis.password}")
    private String password = null;

    @Value("${spring.redis.block-when-exhausted}")
    private Boolean blockWhenExhausted = true;

    @Bean
    public JedisPool redisPoolFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        jedisPoolConfig.setBlockWhenExhausted(blockWhenExhausted);
        JedisPool jedisPool;
        if (StringUtils.isNotBlank(password)) {
            jedisPool  = new JedisPool(jedisPoolConfig, host, port, timeout, password);
            log.info("redis地址：" + host + ":" + port + "有密码");
        } else {
            jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout);
            log.info("redis地址：" + host + ":" + port);
        }
        log.info("JedisPool注入成功！！");
        return jedisPool;
    }

}
