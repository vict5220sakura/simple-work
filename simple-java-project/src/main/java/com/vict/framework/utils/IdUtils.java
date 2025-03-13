package com.vict.framework.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.vict.framework.Constants;
import com.vict.framework.FrameworkCommon;
import com.vict.framework.utils.lock.LockApi;
import com.vict.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ID生成器工具类
 *
 * @author ruoyi
 */
@Slf4j
@Component
@Order(2)
@DependsOn(Constants.framworkStringRedisTemplate)
public class IdUtils {

    private static Snowflake snowflake = IdUtil.getSnowflake(0, 0);

    @PostConstruct
    public void init(){
        log.info("初始化id生成器");
        StringRedisTemplate redisClient = RedisUtil.getStringRedisTemplate();
        LockApi lockApi = LockApi.getLockOverThrowException("IdUtilsInit", 5L * 60L);
        final AtomicInteger workid = new AtomicInteger(RandomUtil.randomInt(0, 31));
        final AtomicInteger datacenterId = new AtomicInteger(RandomUtil.randomInt(0, 31));

        if(FrameworkCommon.redis_enable){
            try{
                lockApi.lockBlock();

                String json = redisClient.opsForValue().get(FrameworkCommon.springApplicationName + "_IdUtilsInit_json");
                JSONObject jsonObject = Optional.ofNullable(json).map(o -> o.trim()).map(o -> JSONObject.parseObject(o)).orElse(new JSONObject());

                while(true){
                    Long overTimestamp = jsonObject.getLong(workid.get() + "-" + datacenterId.get());
                    if(overTimestamp == null){
                        jsonObject.put(workid.get() + "-" + datacenterId.get(), System.currentTimeMillis() + 10L * 60L * 1000L);
                        redisClient.opsForValue().set(FrameworkCommon.springApplicationName + "_IdUtilsInit_json", jsonObject.toJSONString());
                        break;
                    }else if(overTimestamp <= System.currentTimeMillis()){
                        jsonObject.put(workid.get() + "-" + datacenterId.get(), System.currentTimeMillis() + 10L * 60L * 1000L);
                        redisClient.opsForValue().set(FrameworkCommon.springApplicationName + "_IdUtilsInit_json", jsonObject.toJSONString());
                        break;
                    }else{
                        int datacenterIdi = datacenterId.get() + 1;
                        if(datacenterIdi == 32){
                            datacenterId.set(0);
                            int workidi = workid.get() + 1;
                            if(workidi == 32){
                                workid.set(0);
                            }else{
                                workid.set(workidi);
                            }
                        }else{
                            datacenterId.set(datacenterIdi);
                        }
                    }
                }

            }finally{
                lockApi.unlockIfSuccess();
            }
        }


        IdUtils.snowflake = IdUtil.getSnowflake(workid.get(), datacenterId.get());

        Executors.newWorkStealingPool(1).submit(()->{
           while(true){
               Thread.sleep(5L * 60L * 1000L);
               LockApi lockApiIn = LockApi.getLockOverThrowException("IdUtilsInit", 5L * 60L);
               try{
                   lockApiIn.lockBlock();

                   String json = redisClient.opsForValue().get(FrameworkCommon.springApplicationName + "_IdUtilsInit_json");
                   JSONObject jsonObject = Optional.ofNullable(json).map(o -> o.trim()).map(o -> JSONObject.parseObject(o)).orElse(new JSONObject());
                   jsonObject.put(workid.get() + "-" + datacenterId.get(), System.currentTimeMillis() + 10L * 60L * 1000L);
                   redisClient.opsForValue().set(FrameworkCommon.springApplicationName + "_IdUtilsInit_json", jsonObject.toJSONString());

               }finally{
                   lockApiIn.unlockIfSuccess();
               }
           }
        });
        log.info("初始化id生成器完毕workid={}, datacenterId={}", workid, datacenterId);
    }

    /**
     * 获取longid
     */
    public static long snowflakeId(){
        return IdUtils.snowflake.nextId();
    }



    /**
     * 生成salt的静态方法
     *
     * @param n 位数
     */
    public static String getSalt(int n, char[] chars) {
        if(chars == null){
            chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()".toCharArray();
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            char aChar = chars[new Random().nextInt(chars.length)];
            sb.append(aChar);
        }
        return sb.toString();
    }
}
