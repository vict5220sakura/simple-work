package com.vict.framework.utils.cache.core.impl;

import com.vict.framework.utils.cache.core.CacheApi;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CacheJdk implements CacheApi {

    @Data
    private class CacheJdkValue {
        private String value;
        private Long overTime;
    }

    private static ConcurrentHashMap<String, CacheJdkValue> cache = new ConcurrentHashMap<String, CacheJdkValue>();

    /*** 过期处理 */
    private void overTime(String key){
        CacheJdkValue cacheJdkValue = cache.get(key);
        if(cacheJdkValue != null){
            if(cacheJdkValue.getOverTime() != null){
                long now = System.currentTimeMillis();
                if(cacheJdkValue.getOverTime() < now){
                    deleteCache(key);
                }
            }
        }
    }

    @Override
    public void addCache(String key, String value, Long timeMillSeconds) {
        long now = System.currentTimeMillis();
        CacheJdkValue cacheJdkValue = new CacheJdkValue();
        cacheJdkValue.setValue(value);
        if(timeMillSeconds != null){
            cacheJdkValue.setOverTime(now + timeMillSeconds);
        }
        cache.put(key, cacheJdkValue);
    }

    @Override
    public void deleteCache(String key) {
        cache.remove(key);
    }

    @Override
    public String selectCache(String key) {
        overTime(key);
        String value = Optional.ofNullable(cache.get(key)).map(o -> o.getValue()).orElse(null);
        return value;
    }
}
