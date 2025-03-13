package com.vict.framework.utils.lock;

import com.vict.utils.RedisUtil;
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

@Slf4j
@Component
@Order(999)
public class RedisLockDynamic implements LockApi {

    // 计划修复领取守护线程池
    public static ExecutorService redisLockDynamicDeamon = Executors.newFixedThreadPool(1);

    private static final StringRedisTemplate redisClient = RedisUtil.getStringRedisTemplate();

    private static final Long DEFAULT_TIME_SECONDS = 60L; // 系统默认时间秒
    private static final String DEFAULT_LOCKVALUE = "1";

    /**
     * 锁守护线程对象集合
     */
    public static Map<String, RedisLockDynamic> lockMap = new ConcurrentHashMap<String, RedisLockDynamic>();
    /**
     * 启动锁守护线程
     */
    @PostConstruct
    public void doExecute() {
        redisLockDynamicDeamon.submit(()->{
            while(true){
                try {
                    Thread.sleep(DEFAULT_TIME_SECONDS * 1000L / 4L);
                    Set<String> keySet = RedisLockDynamic.lockMap.keySet();
                    if(keySet == null || keySet.size() == 0){
                        continue;
                    }
                    for(String key : keySet){
                        try{
                            RedisLockDynamic redisLockdynamic = RedisLockDynamic.lockMap.get(key);
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
        RedisLockDynamic that = (RedisLockDynamic) o;
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


    public static RedisLockDynamic getLock(String lockKey){
        return new RedisLockDynamic(lockKey);
    }

    /**
     * 强制指定报错时间
     */
    public static RedisLockDynamic getLockOverThrowException(String lockKey, Long overSeconds){
        RedisLockDynamic redisLockDynamic = new RedisLockDynamic(lockKey);
        if(overSeconds != null){
            redisLockDynamic.timeOutSeconds = overSeconds; // 超时报错时间
        }
        return redisLockDynamic;
    }

    /**
     * 强制指定过期时间, 不指定则系统时间内永久有效, 直到系统崩溃
     */
    public static RedisLockDynamic getLockAutoRelease(String lockKey, Long autoReleaseSeconds){
        RedisLockDynamic redisLockDynamic = new RedisLockDynamic(lockKey);
        if(autoReleaseSeconds != null){
            redisLockDynamic.expireTimeForceSeconds = autoReleaseSeconds;
        }
        return redisLockDynamic;
    }

    /**
     * 强制指定过期时间和报错时间
     */
    public static RedisLockDynamic getLockAutoReleaseAndOverThrowException(String lockKey, Long autoReleaseSeconds, Long overSeconds){
        RedisLockDynamic redisLockDynamic = new RedisLockDynamic(lockKey);
        if(autoReleaseSeconds != null){
            redisLockDynamic.expireTimeForceSeconds = autoReleaseSeconds;
        }
        if(overSeconds != null){
            redisLockDynamic.timeOutSeconds = overSeconds; // 超时报错时间
        }
        return redisLockDynamic;
    }

    private RedisLockDynamic(){}
    private RedisLockDynamic(String lockKey) {
        this.lockKey = lockKey;
    }

    @Override
    public void lockBlock() {
        if (redisClient.opsForValue().setIfAbsent(lockKey, DEFAULT_LOCKVALUE, (expireTimeForceSeconds != null ? expireTimeForceSeconds : DEFAULT_TIME_SECONDS), TimeUnit.SECONDS)) {
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
                lockSuccess();
                return;
            }
            spendMillSecs = System.currentTimeMillis() - start;
        }
        throw new LockThrowException();
    }

    @Override
    public boolean lockNotBlock(){
        if (redisClient.opsForValue().setIfAbsent(lockKey, DEFAULT_LOCKVALUE, (expireTimeForceSeconds != null ? expireTimeForceSeconds : DEFAULT_TIME_SECONDS), TimeUnit.SECONDS)) {
            lockSuccess();
            return true;
        }else{
            return false;
        }
    }

    /**
     * 加锁成功动作
     */
    private void lockSuccess() {
        lockSuccessFlag = true;
        this.lockTimeStamp = System.currentTimeMillis(); // 锁时间
        if (this.expireTimeForceSeconds == null) { // 没有指定过期时间 则加入到守护线程中
            RedisLockDynamic.lockMap.put(this.lockKey, this);
        }
    }

    /**
     * 释放锁 只有加锁成功才会释放锁
     */
    @Override
    public void unlockIfSuccess() {
        if(lockSuccessFlag){
            try{
                RedisLockDynamic.lockMap.remove(this.lockKey);
                redisClient.delete(lockKey);
            }catch(Exception e){
                log.error("RedisLock,锁释放异常", e);
            }
        }
    }

    public static void unlockIfSuccess(LockApi lock){
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
