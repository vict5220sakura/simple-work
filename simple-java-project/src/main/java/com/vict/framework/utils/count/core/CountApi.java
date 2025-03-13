package com.vict.framework.utils.count.core;

public interface CountApi {
    Long getNumMain(String redisKeyStr);
    Long nextNumMain(String redisKeyStr);
    void delete(String redisKeyStr);
    void increment(String redisKeyStr, Long num);
}
