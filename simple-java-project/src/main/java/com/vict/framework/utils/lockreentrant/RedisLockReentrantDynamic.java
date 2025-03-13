package com.vict.framework.utils.lockreentrant;

import com.vict.utils.RedisUtil;
import com.vict.utils.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
@Slf4j
@Component
@Order(999)
public class RedisLockReentrantDynamic implements LockReentrantApi {

    public static synchronized Map<String, AtomicInteger> getCountMap(){
        Map<String, AtomicInteger> countMap = Optional.ofNullable(ThreadUtil.getThreadVariable(LockReentrantApi.lockThreadParamKey))
                .map(o -> (Map<String, AtomicInteger>) o).orElse(null);
        if(countMap == null){
            Map<String, AtomicInteger> map = new ConcurrentHashMap<String, AtomicInteger>();
            ThreadUtil.putThreadVariable(LockReentrantApi.lockThreadParamKey, map);
            return map;
        }else{
            return countMap;
        }
    }

    public static synchronized void setCountMap(Map<String, AtomicInteger> map){
        ThreadUtil.putThreadVariable(LockReentrantApi.lockThreadParamKey, map);
    }

    public static int thisThreadCountGet(String key){
        Map<String, AtomicInteger> countMap = getCountMap();
        AtomicInteger atomicInteger = countMap.getOrDefault(key, new AtomicInteger(0));
        return atomicInteger.get();
    }

    public static int thisThreadCountIncAndGet(String key){
        Map<String, AtomicInteger> countMap = getCountMap();
        countMap.putIfAbsent(key, new AtomicInteger(0));
        AtomicInteger atomicInteger = countMap.get(key);
        return atomicInteger.incrementAndGet();
    }

    public static int thisThreadCountDecAndGet(String key){
        Map<String, AtomicInteger> countMap = getCountMap();
        AtomicInteger atomicInteger = countMap.getOrDefault(key, new AtomicInteger(0));
        return atomicInteger.decrementAndGet();
    }

    // 计划修复领取守护线程池
    public static ExecutorService redisLockDynamicDeamon = Executors.newFixedThreadPool(1);

    private static final StringRedisTemplate redisClient = RedisUtil.getStringRedisTemplate();

    private static final Long DEFAULT_TIME_SECONDS = 60L; // 系统默认时间秒
    private static final String DEFAULT_LOCKVALUE = "1";

    /**
     * 锁守护线程对象集合
     */
    public static Map<String, RedisLockReentrantDynamic> lockMap = new ConcurrentHashMap<String, RedisLockReentrantDynamic>();
    /**
     * 启动锁守护线程
     */
    @PostConstruct
    public void doExecute() {
        redisLockDynamicDeamon.submit(()->{
            while(true){
                try {
                    Thread.sleep(DEFAULT_TIME_SECONDS * 1000L / 4L);
                    Set<String> keySet = RedisLockReentrantDynamic.lockMap.keySet();
                    if(keySet == null || keySet.size() == 0){
                        continue;
                    }
                    for(String key : keySet){
                        try{
                            RedisLockReentrantDynamic redisLockdynamic = RedisLockReentrantDynamic.lockMap.get(key);
                            if(redisLockdynamic != null){
                                redisLockdynamic.resetExpireTime();
                            }
                        }catch(Exception e){
                            log.error("redis延长锁时间异常", e);
                        }
                    }

                }catch(Exception e){
                    log.error("RedisLockNew 睡眠异常", e);
                }
            }
        });
    }

    private String lockKey; // 锁key

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RedisLockReentrantDynamic that = (RedisLockReentrantDynamic) o;
        return lockKey.equals(that.lockKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lockKey);
    }

    @Override
    public String getLockKey() {
        return this.lockKey;
    }

    private Long timeOutSeconds = 10L; // 报错时间 默认10秒
    private Long expireTimeForceSeconds = null; // 强制指定过期时间 不指定则永久有效直到服务器挂掉

    private long lockTimeStamp; // 加锁时刻
    private boolean lockSuccessFlag = false; // 加锁成功标志


    public static RedisLockReentrantDynamic getLock(String lockKey){
        return new RedisLockReentrantDynamic(lockKey);
    }

    /**
     * 强制指定报错时间
     */
    public static RedisLockReentrantDynamic getLockOverThrowException(String lockKey, Long overSeconds){
        RedisLockReentrantDynamic redisLockDynamic = new RedisLockReentrantDynamic(lockKey);
        if(overSeconds != null){
            redisLockDynamic.timeOutSeconds = overSeconds; // 超时报错时间
        }
        return redisLockDynamic;
    }

    // /**
    //  * 强制指定过期时间, 不指定则系统时间内永久有效, 直到系统崩溃
    //  */
    // public static RedisLockReentrantDynamic getLockAutoRelease(String lockKey, Long autoReleaseSeconds){
    //     RedisLockReentrantDynamic redisLockDynamic = new RedisLockReentrantDynamic(lockKey);
    //     if(autoReleaseSeconds != null){
    //         redisLockDynamic.expireTimeForceSeconds = autoReleaseSeconds;
    //     }
    //     return redisLockDynamic;
    // }

    // /**
    //  * 强制指定过期时间和报错时间
    //  */
    // public static RedisLockReentrantDynamic getLockAutoReleaseAndOverThrowException(String lockKey, Long autoReleaseSeconds, Long overSeconds){
    //     RedisLockReentrantDynamic redisLockDynamic = new RedisLockReentrantDynamic(lockKey);
    //     if(autoReleaseSeconds != null){
    //         redisLockDynamic.expireTimeForceSeconds = autoReleaseSeconds;
    //     }
    //     if(overSeconds != null){
    //         redisLockDynamic.timeOutSeconds = overSeconds; // 超时报错时间
    //     }
    //     return redisLockDynamic;
    // }

    private RedisLockReentrantDynamic(){}
    private RedisLockReentrantDynamic(String lockKey) {
        this.lockKey = lockKey;
    }

    @Override
    public synchronized void lockBlock() {
        int i = thisThreadCountGet(lockKey);
        if(i == 0){ // 不是当前线程加锁

        }else{ // 当前线程加锁
            thisThreadCountIncAndGet(lockKey); // 当前线程计数器+1
            lockSuccessReentrantDynamic();
            return;
        }

        if (redisClient.opsForValue().setIfAbsent(lockKey, DEFAULT_LOCKVALUE, (expireTimeForceSeconds != null ? expireTimeForceSeconds : DEFAULT_TIME_SECONDS), TimeUnit.SECONDS)) {
            // 加锁成功
            thisThreadCountIncAndGet(lockKey);
            lockSuccess();
            return ;
        }
        long start = System.currentTimeMillis();
        long spendMillSecs = 0L;
        while (spendMillSecs < (timeOutSeconds * 1000L)) {
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (redisClient.opsForValue().setIfAbsent(lockKey, DEFAULT_LOCKVALUE, (expireTimeForceSeconds != null ? expireTimeForceSeconds : DEFAULT_TIME_SECONDS), TimeUnit.SECONDS)) {
                thisThreadCountIncAndGet(lockKey);
                lockSuccess();
                return;
            }
            spendMillSecs = System.currentTimeMillis() - start;
        }
        throw new LockThrowException();
    }

    @Override
    public synchronized boolean lockNotBlock(){
        int i = thisThreadCountGet(lockKey);
        if(i == 0){ // 不是当前线程加锁

        }else{ // 当前线程加锁
            thisThreadCountIncAndGet(lockKey); // 当前线程计数器+1
            lockSuccessReentrantDynamic();
            return true;
        }

        if (redisClient.opsForValue().setIfAbsent(lockKey, DEFAULT_LOCKVALUE, (expireTimeForceSeconds != null ? expireTimeForceSeconds : DEFAULT_TIME_SECONDS), TimeUnit.SECONDS)) {
            thisThreadCountIncAndGet(lockKey); // 当前线程计数器+1
            lockSuccess();
            return true;
        }else{
            return false;
        }
    }

    private void lockSuccessReentrantDynamic(){
        lockSuccessFlag = true;
        this.lockTimeStamp = System.currentTimeMillis(); // 锁时间
    }

    /**
     * 加锁成功动作
     */
    private void lockSuccess() {
        lockSuccessFlag = true;
        this.lockTimeStamp = System.currentTimeMillis(); // 锁时间
        if (this.expireTimeForceSeconds == null) { // 没有指定过期时间 则加入到守护线程中
            RedisLockReentrantDynamic.lockMap.put(this.lockKey, this);
        }
    }

    /**
     * 释放锁 只有加锁成功才会释放锁
     */
    @Override
    public void unlockIfSuccess() {
        if(lockSuccessFlag){
            try{
                int i = thisThreadCountDecAndGet(this.lockKey);
                if(i <= 0){
                    RedisLockReentrantDynamic.lockMap.remove(this.lockKey);
                    redisClient.delete(lockKey);
                }
            }catch(Exception e){
                log.error("RedisLock,锁释放异常", e);
            }
        }
    }

    public static void unlockIfSuccess(LockReentrantApi lock){
        Optional.ofNullable(lock).ifPresent(o-> o.unlockIfSuccess());
    }

    /**
     * 锁自身时间延长 只有加锁成功才会延长并且没有强制指定过期时间
     */
    public void resetExpireTime() {
        try{
            if(lockSuccessFlag && expireTimeForceSeconds == null){
                long now = System.currentTimeMillis();
                if((now - lockTimeStamp) > (DEFAULT_TIME_SECONDS * 1000L / 2L)){
                    redisClient.expire(lockKey, DEFAULT_TIME_SECONDS, TimeUnit.SECONDS);
                }
            }
        }catch(Exception e){
            log.error("重置锁过期时间一长", e);
        }
    }


}
