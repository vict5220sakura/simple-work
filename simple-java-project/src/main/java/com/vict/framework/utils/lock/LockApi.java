package com.vict.framework.utils.lock;

import com.vict.framework.FrameworkCommon;
import com.vict.framework.utils.lock.entity.MysqlLock;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface LockApi {

    class LockThrowException extends RuntimeException{ }
    class NoLockThrowException extends RuntimeException{ }

    String getLockKey();

    void lockBlock();

    void unlockIfSuccess();

    boolean lockNotBlock();

    static void unlockIfSuccess(LockApi lockApi){
        Optional.ofNullable(lockApi).ifPresent(o-> o.unlockIfSuccess());
    }

    static LockApi getLock(String lockKey){
        if(FrameworkCommon.lockImpl.equals(FrameworkCommon.lockImpl_jdk)){
            return LockJdk.getLock(FrameworkCommon.lockPrx + lockKey);
        }else if(FrameworkCommon.lockImpl.equals(FrameworkCommon.lockImpl_redis)){
            return RedisLockDynamic.getLock(FrameworkCommon.lockPrx + lockKey);
        }else if(FrameworkCommon.lockImpl.equals(FrameworkCommon.lockImpl_mysql)){
            return LockMysql.getLock(FrameworkCommon.lockPrx + lockKey);
        }
        throw new NoLockThrowException();
    }

    static LockApi getLockByType(String lockKey, String lockTypeStr){
        String lockType = lockTypeStr;
        if(lockType == null){
            lockType = FrameworkCommon.lockImpl;
        }

        if(lockType.equals(FrameworkCommon.lockImpl_jdk)){
            return LockJdk.getLock(FrameworkCommon.lockPrx + lockKey);
        }else if(lockType.equals(FrameworkCommon.lockImpl_redis)){
            return RedisLockDynamic.getLock(FrameworkCommon.lockPrx + lockKey);
        }else if(FrameworkCommon.lockImpl.equals(FrameworkCommon.lockImpl_mysql)){
            return LockMysql.getLock(FrameworkCommon.lockPrx + lockKey);
        }
        throw new NoLockThrowException();
    }

    /**
     * 强制指定报错时间
     */
    static LockApi getLockOverThrowException(String lockKey, Long overSeconds){
        if(FrameworkCommon.lockImpl.equals(FrameworkCommon.lockImpl_jdk)){
            return LockJdk.getLockOverThrowException(FrameworkCommon.lockPrx + lockKey, overSeconds);
        }else if(FrameworkCommon.lockImpl.equals(FrameworkCommon.lockImpl_redis)){
            return RedisLockDynamic.getLockOverThrowException(FrameworkCommon.lockPrx + lockKey, overSeconds);
        }else if(FrameworkCommon.lockImpl.equals(FrameworkCommon.lockImpl_mysql)){
            return LockMysql.getLockOverThrowException(FrameworkCommon.lockPrx + lockKey, overSeconds);
        }
        throw new NoLockThrowException();
    }

    /**
     * 强制指定报错时间
     */
    static LockApi getLockOverThrowExceptionByType(String lockKey, Long overSeconds, String lockTypeStr){
        String lockType = lockTypeStr;
        if(lockType == null){
            lockType = FrameworkCommon.lockImpl;
        }

        if(lockType.equals(FrameworkCommon.lockImpl_jdk)){
            return LockJdk.getLockOverThrowException(FrameworkCommon.lockPrx + lockKey, overSeconds);
        }else if(lockType.equals(FrameworkCommon.lockImpl_redis)){
            return RedisLockDynamic.getLockOverThrowException(FrameworkCommon.lockPrx + lockKey, overSeconds);
        }else if(FrameworkCommon.lockImpl.equals(FrameworkCommon.lockImpl_mysql)){
            return LockMysql.getLockOverThrowException(FrameworkCommon.lockPrx + lockKey, overSeconds);
        }
        throw new NoLockThrowException();
    }

    /**
     * 强制指定过期时间
     */
    static LockApi getLockAutoRelease(String lockKey, Long autoReleaseSeconds){
        if(FrameworkCommon.lockImpl.equals(FrameworkCommon.lockImpl_jdk)){
            return LockJdk.getLockAutoRelease(FrameworkCommon.lockPrx + lockKey, autoReleaseSeconds);
        }else if(FrameworkCommon.lockImpl.equals(FrameworkCommon.lockImpl_redis)){
            return RedisLockDynamic.getLockAutoRelease(FrameworkCommon.lockPrx + lockKey, autoReleaseSeconds);
        }else if(FrameworkCommon.lockImpl.equals(FrameworkCommon.lockImpl_mysql)){
            return LockMysql.getLockAutoRelease(FrameworkCommon.lockPrx + lockKey, autoReleaseSeconds);
        }
        throw new NoLockThrowException();
    }

    /**
     * 强制指定过期时间
     */
    static LockApi getLockAutoReleaseByType(String lockKey, Long autoReleaseSeconds, String lockTypeStr){

        String lockType = lockTypeStr;
        if(lockType == null){
            lockType = FrameworkCommon.lockImpl;
        }

        if(lockType.equals(FrameworkCommon.lockImpl_jdk)){
            return LockJdk.getLockAutoRelease(FrameworkCommon.lockPrx + lockKey, autoReleaseSeconds);
        }else if(lockType.equals(FrameworkCommon.lockImpl_redis)){
            return RedisLockDynamic.getLockAutoRelease(FrameworkCommon.lockPrx + lockKey, autoReleaseSeconds);
        }else if(FrameworkCommon.lockImpl.equals(FrameworkCommon.lockImpl_mysql)){
            return LockMysql.getLockAutoRelease(FrameworkCommon.lockPrx + lockKey, autoReleaseSeconds);
        }
        throw new NoLockThrowException();
    }

    /**
     * 强制指定过期时间和报错时间
     */
    static LockApi getLockAutoReleaseAndOverThrowException(String lockKey, Long autoReleaseSeconds, Long overSeconds){
        if(FrameworkCommon.lockImpl.equals(FrameworkCommon.lockImpl_jdk)){
            return LockJdk.getLockAutoReleaseAndOverThrowException(FrameworkCommon.lockPrx + lockKey, autoReleaseSeconds, overSeconds);
        }else if(FrameworkCommon.lockImpl.equals(FrameworkCommon.lockImpl_redis)){
            return RedisLockDynamic.getLockAutoReleaseAndOverThrowException(FrameworkCommon.lockPrx + lockKey, autoReleaseSeconds, overSeconds);
        }else if(FrameworkCommon.lockImpl.equals(FrameworkCommon.lockImpl_mysql)){
            return LockMysql.getLockAutoReleaseAndOverThrowException(FrameworkCommon.lockPrx + lockKey, autoReleaseSeconds, overSeconds);
        }
        throw new NoLockThrowException();
    }

    /**
     * 强制指定过期时间和报错时间
     */
    static LockApi getLockAutoReleaseAndOverThrowExceptionByType(String lockKey, Long autoReleaseSeconds, Long overSeconds, String lockTypeStr){

        String lockType = lockTypeStr;
        if(lockType == null){
            lockType = FrameworkCommon.lockImpl;
        }

        if(lockType.equals(FrameworkCommon.lockImpl_jdk)){
            return LockJdk.getLockAutoReleaseAndOverThrowException(FrameworkCommon.lockPrx + lockKey, autoReleaseSeconds, overSeconds);
        }else if(lockType.equals(FrameworkCommon.lockImpl_redis)){
            return RedisLockDynamic.getLockAutoReleaseAndOverThrowException(FrameworkCommon.lockPrx + lockKey, autoReleaseSeconds, overSeconds);
        }else if(FrameworkCommon.lockImpl.equals(FrameworkCommon.lockImpl_mysql)){
            return LockMysql.getLockAutoReleaseAndOverThrowException(FrameworkCommon.lockPrx + lockKey, autoReleaseSeconds, overSeconds);
        }
        throw new NoLockThrowException();
    }

    static LockList getLockList(LockApi ...locks){
        LockList lockList = new LockList();

        List<LockApi> redisLockDynamics = Optional.ofNullable(locks).filter(o -> o.length > 0).map(o -> Arrays.asList(o)).orElse(new ArrayList<>());
        lockList.setLockList(redisLockDynamics);

        return lockList;
    }

    @Data
    static class LockList {
        private List<LockApi> lockList;

        public void addLock(LockApi lock){
            Optional.ofNullable(this.lockList).ifPresent(o-> o.add(lock));
        }

        public void lockBlock(){
            if(this.lockList == null || this.lockList.size() <= 0){
                return;
            }
            this.lockList = this.lockList.stream().collect(Collectors.toSet()).stream().collect(Collectors.toList());

            this.lockList.sort((o1, o2) -> {
                if(o1.getLockKey().compareTo(o2.getLockKey()) < 0){
                    return -1;
                }else if(o1.getLockKey().compareTo(o2.getLockKey()) > 0){
                    return 1;
                }else{
                    return 0;
                }
            });

            for(LockApi lock : this.lockList){
                lock.lockBlock();
            }
        }

        public void unlockIfSuccess() {
            this.lockList.sort((o1, o2) -> {
                if(o1.getLockKey().compareTo(o2.getLockKey()) < 0){
                    return -1;
                }else if(o1.getLockKey().compareTo(o2.getLockKey()) > 0){
                    return 1;
                }else{
                    return 0;
                }
            });
            for(LockApi lock : this.lockList){
                lock.unlockIfSuccess();
            }
        }
    }
}
