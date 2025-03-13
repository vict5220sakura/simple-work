package com.vict.framework.redis;

import com.vict.framework.Constants;
import com.vict.framework.FrameworkCommon;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component("redisConfig")
@Order(2)
@DependsOn("frameworkCommon")
public class RedisConfig {

    @Bean(name = Constants.framworkStringRedisTemplate, autowireCandidate = false)
    public StringRedisTemplate stringRedisTemplate(){
        if(FrameworkCommon.redis_enable == false){
            return null;
        }
        // connection config# 如果需要使用cluster或者sentinel，则定义对应的对象即可。
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(FrameworkCommon.redis_host);
        configuration.setPort(FrameworkCommon.redis_port);
        configuration.setPassword(RedisPassword.of(FrameworkCommon.redis_password));
        configuration.setDatabase(FrameworkCommon.redis_database);

        // pool config
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        genericObjectPoolConfig.setMaxTotal(FrameworkCommon.redis_pool_max_active);
        genericObjectPoolConfig.setMinIdle(FrameworkCommon.redis_pool_max_idle);
        genericObjectPoolConfig.setMaxIdle(FrameworkCommon.redis_pool_minIidle);
        genericObjectPoolConfig.setMaxWaitMillis(FrameworkCommon.redis_pool_max_wait);

        // create connection factory
        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder builder = LettucePoolingClientConfiguration.builder();
        builder.poolConfig(genericObjectPoolConfig);
        builder.commandTimeout(Duration.ofSeconds(FrameworkCommon.redis_timeout));
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(
                configuration, builder.build()
        );
        connectionFactory.afterPropertiesSet();

        StringRedisTemplate stringRedisTemplate = createStringRedisTemplate(connectionFactory);

        log.info(Constants.framworkStringRedisTemplate + "初始化完成");

        return stringRedisTemplate;
    }

    private StringRedisTemplate createStringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
