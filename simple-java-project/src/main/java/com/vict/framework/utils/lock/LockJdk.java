package com.vict.framework.utils.lock;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class LockJdk implements LockApi{

    /** 锁核心类 */
    public static class LockCore {
        /** 锁核心实体类 */
        @Data
        public static class LockAtomic{
            private String key;
            private Long lockTimestamp;
            private Long autoReleaseSecond;

            /** 是否应该过期释放 */
            public boolean isOverTime(){
                if(autoReleaseSecond == null){
                    return false;
                }
                if(lockTimestamp + (autoReleaseSecond * 1000L) <= System.currentTimeMillis()){ // 过期
                    return true;
                }else{ // 未过期
                    return false;
                }
            }
        }

        public static final ConcurrentMap<String, LockAtomic> lock_core_map = new ConcurrentHashMap<>();

        /** 加锁常规方法 */
        public boolean lock(String lockKey, Long autoReleaseSecond) {


            LockAtomic lockAtomicAlready = lock_core_map.get(lockKey);
            if(lockAtomicAlready != null){ // 原先有值
                if(lockAtomicAlready.isOverTime()){ // 过期了
                    unlock(lockAtomicAlready); // 释放锁

                    LockAtomic lockAtomic = new LockAtomic();
                    lockAtomic.setKey(lockKey);
                    lockAtomic.setLockTimestamp(System.currentTimeMillis());
                    lockAtomic.setAutoReleaseSecond(autoReleaseSecond);

                    LockAtomic lockAtomicRes = lock_core_map.putIfAbsent(lockAtomic.getKey(), lockAtomic);
                    if(lockAtomicRes == null){
                        return true; // 加锁成功
                    }else{ // 加锁失败
                        return false;
                    }
                }else{ // 未过期 加锁失败
                    return false;
                }
            }else{ // 原先没有值
                LockAtomic lockAtomic = new LockAtomic();
                lockAtomic.setKey(lockKey);
                lockAtomic.setLockTimestamp(System.currentTimeMillis());
                lockAtomic.setAutoReleaseSecond(autoReleaseSecond);

                LockAtomic lockAtomicRes = lock_core_map.putIfAbsent(lockAtomic.getKey(), lockAtomic);
                if(lockAtomicRes == null){
                    return true; // 加锁成功
                }else{ // 加锁失败
                    return false;
                }
            }
        }

        /** 释放锁 */
        public void unlock(String key){
            lock_core_map.remove(key);
        }

        /** 释放锁 */
        public boolean unlock(LockAtomic lockAtomic){
            return lock_core_map.remove(lockAtomic.getKey(), lockAtomic);
        }
    }

    /** 锁核心 */
    private static LockCore lockCore = new LockCore();

    private String lockKey; // 锁key

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LockJdk that = (LockJdk) o;
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
    private Long autoReleaseSecond = null; // 强制指定过期时间 不指定则永久有效直到服务器挂掉

    private long lockTimestamp; // 加锁时刻
    private boolean lockSuccessFlag = false; // 加锁成功标志

    public static LockJdk getLock(String lockKey){
        return new LockJdk(lockKey);
    }

    /**
     * 强制指定报错时间
     */
    public static LockJdk getLockOverThrowException(String lockKey, Long overSeconds){
        LockJdk lockJdk = new LockJdk(lockKey);
        if(overSeconds != null){
            lockJdk.throwExceptionSecond = overSeconds; // 超时报错时间
        }
        return lockJdk;
    }

    /**
     * 强制指定过期时间
     */
    public static LockJdk getLockAutoRelease(String lockKey, Long autoReleaseSeconds){
        LockJdk lockJdk = new LockJdk(lockKey);
        if(autoReleaseSeconds != null){
            lockJdk.autoReleaseSecond = autoReleaseSeconds;
        }
        return lockJdk;
    }

    /**
     * 强制指定过期时间和报错时间
     */
    public static LockJdk getLockAutoReleaseAndOverThrowException(String lockKey, Long autoReleaseSeconds, Long overSeconds){
        LockJdk lockJdk = new LockJdk(lockKey);
        if(autoReleaseSeconds != null){
            lockJdk.autoReleaseSecond = autoReleaseSeconds;
        }
        if(overSeconds != null){
            lockJdk.throwExceptionSecond = overSeconds; // 超时报错时间
        }
        return lockJdk;
    }

    private LockJdk(){}
    private LockJdk(String lockKey) {
        this.lockKey = lockKey;
    }

    @Override
    public void lockBlock() {
        if (lockCore.lock(lockKey, autoReleaseSecond)) {
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
            if (lockCore.lock(lockKey, autoReleaseSecond)) {
                lockSuccess();
                return;
            }
            spendMillSecs = System.currentTimeMillis() - start;
        }
        throw new LockThrowException();
    }

    @Override
    public boolean lockNotBlock(){
        if (lockCore.lock(lockKey, autoReleaseSecond)) {
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
        this.lockTimestamp = System.currentTimeMillis(); // 锁时间
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

    public static void unlockIfSuccess(LockJdk lockJdk){
        Optional.ofNullable(lockJdk).ifPresent(o-> o.unlockIfSuccess());
    }
}
