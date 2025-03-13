package com.vict.framework.utils.cache.core.impl;

import com.vict.framework.Constants;
import com.vict.framework.utils.cache.core.CacheApi;
import com.vict.utils.RedisUtil;
import com.vict.utils.SpringUtils;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@DependsOn("redisConfig")
@Component
public class CacheRedis implements CacheApi {

    private static StringRedisTemplate redisClient;

    @PostConstruct
    public void init(){
        redisClient = RedisUtil.getStringRedisTemplate();
    }

    @Override
    public void addCache(String key, String value, Long timeMillSeconds) {
        redisClient.opsForValue().set(key, value);
        if(timeMillSeconds != null){
            redisClient.expire(key, timeMillSeconds, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void deleteCache(String key) {
        redisClient.delete(key);
    }

    @Override
    public String selectCache(String key) {
        return redisClient.opsForValue().get(key);
    }
}
