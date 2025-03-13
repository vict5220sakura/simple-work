package com.vict.framework.utils.lockreentrant;

import com.vict.utils.ThreadUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
public class LockReentrantJdk implements LockReentrantApi {

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

    /** 锁核心类 */
    public static class LockCore {
        /** 锁核心实体类 */
        @Data
        public static class LockAtomic{
            private String key;
        }

        public static final ConcurrentMap<String, LockAtomic> lock_core_map = new ConcurrentHashMap<>();

        /** 加锁常规方法 */
        public boolean lock(String lockKey) {
            LockAtomic lockAtomicAlready = lock_core_map.get(lockKey);
            if(lockAtomicAlready != null){ // 原先有值
                int i = thisThreadCountGet(lockKey);
                if(i == 0){ // 不是当前线程加锁
                    return false;
                }else{ // 当前线程加锁
                    thisThreadCountIncAndGet(lockKey); // 当前线程计数器+1
                    return true;
                }
            }else{ // 原先没有值
                LockAtomic lockAtomic = new LockAtomic();
                lockAtomic.setKey(lockKey);

                LockAtomic lockAtomicRes = lock_core_map.putIfAbsent(lockAtomic.getKey(), lockAtomic);
                if(lockAtomicRes == null){
                    thisThreadCountIncAndGet(lockKey);
                    return true; // 加锁成功
                }else{ // 加锁失败
                    int i = thisThreadCountGet(lockKey);
                    if(i == 0){ // 不是当前线程加锁
                        return false;
                    }else{ // 当前线程加锁
                        thisThreadCountIncAndGet(lockKey); // 当前线程计数器+1
                        return true;
                    }
                }
            }
        }

        /** 释放锁 */
        public void unlock(String key){
            int i = thisThreadCountDecAndGet(key);
            if(i <= 0){
                lock_core_map.remove(key);
            }
        }

        /** 释放锁 */
        public boolean unlock(LockAtomic lockAtomic){
            int i = thisThreadCountDecAndGet(lockAtomic.getKey());
            if(i <= 0){
                return lock_core_map.remove(lockAtomic.getKey(), lockAtomic);
            }else{
                return false;
            }
        }
    }

    /** 锁核心 */
    private static LockCore lockCore = new LockCore();

    private String lockKey; // 锁key

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LockReentrantJdk that = (LockReentrantJdk) o;
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

    private Long throwExceptionSecond = 10L; // 抛出异常时间 默认10秒
    // private Long autoReleaseSecond = null; // 强制指定过期时间 不指定则永久有效直到服务器挂掉

    // private long lockTimestamp; // 加锁时刻
    private boolean lockSuccessFlag = false; // 加锁成功标志

    public static LockReentrantJdk getLock(String lockKey){
        return new LockReentrantJdk(lockKey);
    }

    /**
     * 强制指定报错时间
     */
    public static LockReentrantJdk getLockOverThrowException(String lockKey, Long overSeconds){
        LockReentrantJdk lockReentrantJdk = new LockReentrantJdk(lockKey);
        if(overSeconds != null){
            lockReentrantJdk.throwExceptionSecond = overSeconds; // 超时报错时间
        }
        return lockReentrantJdk;
    }

    // /**
    //  * 强制指定过期时间
    //  */
    // public static LockReentrantJdk getLockAutoRelease(String lockKey, Long autoReleaseSeconds){
    //     LockReentrantJdk lockReentrantJdk = new LockReentrantJdk(lockKey);
    //     if(autoReleaseSeconds != null){
    //         lockReentrantJdk.autoReleaseSecond = autoReleaseSeconds;
    //     }
    //     return lockReentrantJdk;
    // }

    // /**
    //  * 强制指定过期时间和报错时间
    //  */
    // public static LockReentrantJdk getLockAutoReleaseAndOverThrowException(String lockKey, Long autoReleaseSeconds, Long overSeconds){
    //     LockReentrantJdk lockReentrantJdk = new LockReentrantJdk(lockKey);
    //     if(autoReleaseSeconds != null){
    //         lockReentrantJdk.autoReleaseSecond = autoReleaseSeconds;
    //     }
    //     if(overSeconds != null){
    //         lockReentrantJdk.throwExceptionSecond = overSeconds; // 超时报错时间
    //     }
    //     return lockReentrantJdk;
    // }

    private LockReentrantJdk(){}
    private LockReentrantJdk(String lockKey) {
        this.lockKey = lockKey;
    }

    @Override
    public void lockBlock() {
        if (lockCore.lock(lockKey)) {
            lockSuccess();
            return ;
        }
        long start = System.currentTimeMillis();
        long spendMillSecs = 0L;
        while (spendMillSecs < (throwExceptionSecond * 1000L)) {
            try {
                Thread.sleep(50L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (lockCore.lock(lockKey)) {
                lockSuccess();
                return;
            }
            spendMillSecs = System.currentTimeMillis() - start;
        }
        throw new LockThrowException();
    }

    @Override
    public boolean lockNotBlock(){
        if (lockCore.lock(lockKey)) {
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
        // this.lockTimestamp = System.currentTimeMillis(); // 锁时间
    }

    /**
     * 释放锁 只有加锁成功才会释放锁
     */
    @Override
    public void unlockIfSuccess() {
        if(lockSuccessFlag){
            try{
                lockCore.unlock(lockKey);
            }catch(Exception e){
                log.error("RedisLock,锁释放异常", e);
            }
        }
    }

    public static void unlockIfSuccess(LockReentrantJdk lockReentrantJdk){
        Optional.ofNullable(lockReentrantJdk).ifPresent(o-> o.unlockIfSuccess());
    }
}
