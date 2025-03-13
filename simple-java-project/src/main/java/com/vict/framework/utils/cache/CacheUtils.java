package com.vict.framework.utils.cache;

import com.alibaba.fastjson.JSONObject;
import com.vict.framework.FrameworkCommon;
import com.vict.framework.utils.cache.core.CacheApi;
import com.vict.framework.utils.cache.core.impl.CacheJdk;
import com.vict.framework.utils.cache.core.impl.CacheRedis;
import com.vict.utils.SpringUtils;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class CacheUtils {

    private static CacheApi cacheApi;

    @PostConstruct
    public void init() {
        if(FrameworkCommon.cacheImpl.equals(FrameworkCommon.cacheImpl_redis)){
            CacheUtils.cacheApi = SpringUtils.getBean(CacheRedis.class);
        }else if(FrameworkCommon.cacheImpl.equals(FrameworkCommon.cacheImpl_jdk)){
            CacheUtils.cacheApi = SpringUtils.getBean(CacheJdk.class);
        }else{
            throw new RuntimeException("cacheImpl配置错误");
        }
    }

    public static void addCache(String key, String value, Long timeMillSeconds){
        cacheApi.addCache(key, value, timeMillSeconds);
    }

    public static void addCache(String key, String value){
        cacheApi.addCache(key, value, null);
    }

    public static <T> void addCache(String key, T t){
        cacheApi.addCache(key, JSONObject.toJSONString(t), null);
    }

    public static void deleteCache(String key){
        cacheApi.deleteCache(key);
    }
    public static String selectCache(String key){
        return cacheApi.selectCache(key);
    }

    public static <T> T selectCache(String key, Class<T> clazz){
        String s = cacheApi.selectCache(key);
        if(s == null){
            return null;
        }
        return JSONObject.parseObject(s, clazz);
    }
}
