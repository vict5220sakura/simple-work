package com.vict.framework.utils.cache.core;

public interface CacheApi {
    void addCache(String key, String value, Long timeMillSeconds);
    void deleteCache(String key);
    String selectCache(String key);
}
