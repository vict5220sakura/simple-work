package com.vict.framework.utils.count.core;

import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class CountJdk implements CountApi{

    private static ConcurrentHashMap<String, AtomicLong> map = new ConcurrentHashMap<String, AtomicLong>();

    @Override
    public Long getNumMain(String redisKeyStr) {
        Long num = Optional.ofNullable(map.get(redisKeyStr)).map(o -> o.get()).orElse(null);
        return num;
    }

    @Override
    public Long nextNumMain(String redisKeyStr) {
        AtomicLong atomicLong = Optional.ofNullable(map.get(redisKeyStr)).orElse(null);
        if(atomicLong == null){
            return null;
        }
        long num = atomicLong.incrementAndGet();
        return num;
    }

    @Override
    public void delete(String redisKeyStr) {
        map.remove(redisKeyStr);
    }

    @Override
    public void increment(String redisKeyStr, Long num) {
        AtomicLong atomicLong = Optional.ofNullable(map.get(redisKeyStr)).orElse(null);
        if(atomicLong == null){
            AtomicLong atomicLongExist = map.putIfAbsent(redisKeyStr, new AtomicLong(num));
            if(atomicLongExist != null){
                atomicLongExist.addAndGet(num);
            }
            return;
        }
        atomicLong.addAndGet(num);
    }
}
