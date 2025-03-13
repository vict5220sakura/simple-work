package com.vict.framework.utils.count.core;

import com.vict.utils.RedisUtil;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Component
@DependsOn("redisConfig")
public class CountRedis implements CountApi{

    private static StringRedisTemplate redis;

    @PostConstruct
    public void init(){
        redis = RedisUtil.getStringRedisTemplate();
    }

    @Override
    public Long getNumMain(String redisKeyStr) {
        String numStrOp = redis.boundValueOps(redisKeyStr).get();
        Long num = Optional.ofNullable(numStrOp).map(o -> o.trim()).filter(o -> !o.equals(""))
                .map(o -> Long.parseLong(o)).orElse(0L);

        if(num == null){
            return null;
        }else if(num == 0L){
            String numStr = redis.boundValueOps(redisKeyStr).get();
            if(numStr == null){
                return null;
            }else{
                return num;
            }
        }else{
            return num;
        }
    }

    @Override
    public Long nextNumMain(String redisKeyStr) {
        Long increment = redis.boundValueOps(redisKeyStr).increment();
        return increment;
    }

    @Override
    public void delete(String redisKeyStr) {
        redis.delete(redisKeyStr);
    }

    @Override
    public void increment(String redisKeyStr, Long num) {
        redis.boundValueOps(redisKeyStr).increment(num); // redis初始化
    }
}
