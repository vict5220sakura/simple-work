package com.vict.framework.utils.lockreentrant;

import com.vict.framework.FrameworkCommon;
import lombok.Data;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 可重入锁, 不支持自动释放
 */
public interface LockReentrantApi {
    public static final String lockThreadParamKey = "lockReentrantCount";
    class LockThrowException extends RuntimeException{ }
    class NoLockThrowException extends RuntimeException{ }

    static Map<String, AtomicInteger> getCountMap(){
        if(FrameworkCommon.lockImpl.equals(FrameworkCommon.lockImpl_jdk)){
            return LockReentrantJdk.getCountMap();
        }else if(FrameworkCommon.lockImpl.equals(FrameworkCommon.lockImpl_redis)){
            return RedisLockReentrantDynamic.getCountMap();
        }
        throw new NoLockThrowException();
    }

    static void setCountMap(Map<String, AtomicInteger> map){
        if(FrameworkCommon.lockImpl.equals(FrameworkCommon.lockImpl_jdk)){
            LockReentrantJdk.setCountMap(map);
            return;
        }else if(FrameworkCommon.lockImpl.equals(FrameworkCommon.lockImpl_redis)){
            RedisLockReentrantDynamic.setCountMap(map);
            return;
        }
        throw new NoLockThrowException();
    }


    String getLockKey();

    void lockBlock();

    void unlockIfSuccess();

    boolean lockNotBlock();

    static void unlockIfSuccess(LockReentrantApi LockReentrantApi){
        Optional.ofNullable(LockReentrantApi).ifPresent(o-> o.unlockIfSuccess());
    }

    static LockReentrantApi getLock(String lockKey){
        if(FrameworkCommon.lockImpl.equals(FrameworkCommon.lockImpl_jdk)){
            return LockReentrantJdk.getLock(FrameworkCommon.lockPrx + lockKey);
        }else if(FrameworkCommon.lockImpl.equals(FrameworkCommon.lockImpl_redis)){
            return RedisLockReentrantDynamic.getLock(FrameworkCommon.lockPrx + lockKey);
        }
        throw new NoLockThrowException();
    }

    static LockReentrantApi getLockByType(String lockKey, String lockTypeStr){
        String lockType = lockTypeStr;
        if(lockType == null){
            lockType = FrameworkCommon.lockImpl;
        }

        if(lockType.equals(FrameworkCommon.lockImpl_jdk)){
            return LockReentrantJdk.getLock(FrameworkCommon.lockPrx + lockKey);
        }else if(lockType.equals(FrameworkCommon.lockImpl_redis)){
            return RedisLockReentrantDynamic.getLock(FrameworkCommon.lockPrx + lockKey);
        }
        throw new NoLockThrowException();
    }

    /**
     * 强制指定报错时间
     */
    static LockReentrantApi getLockOverThrowException(String lockKey, Long overSeconds){
        if(FrameworkCommon.lockImpl.equals(FrameworkCommon.lockImpl_jdk)){
            return LockReentrantJdk.getLockOverThrowException(FrameworkCommon.lockPrx + lockKey, overSeconds);
        }else if(FrameworkCommon.lockImpl.equals(FrameworkCommon.lockImpl_redis)){
            return RedisLockReentrantDynamic.getLockOverThrowException(FrameworkCommon.lockPrx + lockKey, overSeconds);
        }
        throw new NoLockThrowException();
    }

    /**
     * 强制指定报错时间
     */
    static LockReentrantApi getLockOverThrowExceptionByType(String lockKey, Long overSeconds, String lockTypeStr){
        String lockType = lockTypeStr;
        if(lockType == null){
            lockType = FrameworkCommon.lockImpl;
        }

        if(lockType.equals(FrameworkCommon.lockImpl_jdk)){
            return LockReentrantJdk.getLockOverThrowException(FrameworkCommon.lockPrx + lockKey, overSeconds);
        }else if(lockType.equals(FrameworkCommon.lockImpl_redis)){
            return RedisLockReentrantDynamic.getLockOverThrowException(FrameworkCommon.lockPrx + lockKey, overSeconds);
        }
        throw new NoLockThrowException();
    }

    // /**
    //  * 强制指定过期时间
    //  */
    // static LockReentrantApi getLockAutoRelease(String lockKey, Long autoReleaseSeconds){
    //     if(FrameworkCommon.lockImpl.equals(FrameworkCommon.lockImpl_jdk)){
    //         return LockReentrantJdk.getLockAutoRelease(FrameworkCommon.lockPrx + lockKey, autoReleaseSeconds);
    //     }else if(FrameworkCommon.lockImpl.equals(FrameworkCommon.lockImpl_redis)){
    //         return RedisLockReentrantDynamic.getLockAutoRelease(FrameworkCommon.lockPrx + lockKey, autoReleaseSeconds);
    //     }
    //     throw new LockReentrantApi.NoLockThrowException();
    // }

    // /**
    //  * 强制指定过期时间
    //  */
    // static LockReentrantApi getLockAutoReleaseByType(String lockKey, Long autoReleaseSeconds, String lockTypeStr){
    //
    //     String lockType = lockTypeStr;
    //     if(lockType == null){
    //         lockType = FrameworkCommon.lockImpl;
    //     }
    //
    //     if(lockType.equals(FrameworkCommon.lockImpl_jdk)){
    //         return LockReentrantJdk.getLockAutoRelease(FrameworkCommon.lockPrx + lockKey, autoReleaseSeconds);
    //     }else if(lockType.equals(FrameworkCommon.lockImpl_redis)){
    //         return RedisLockReentrantDynamic.getLockAutoRelease(FrameworkCommon.lockPrx + lockKey, autoReleaseSeconds);
    //     }
    //     throw new LockReentrantApi.NoLockThrowException();
    // }

    // /**
    //  * 强制指定过期时间和报错时间
    //  */
    // static LockReentrantApi getLockAutoReleaseAndOverThrowException(String lockKey, Long autoReleaseSeconds, Long overSeconds){
    //     if(FrameworkCommon.lockImpl.equals(FrameworkCommon.lockImpl_jdk)){
    //         return LockReentrantJdk.getLockAutoReleaseAndOverThrowException(FrameworkCommon.lockPrx + lockKey, autoReleaseSeconds, overSeconds);
    //     }else if(FrameworkCommon.lockImpl.equals(FrameworkCommon.lockImpl_redis)){
    //         return RedisLockReentrantDynamic.getLockAutoReleaseAndOverThrowException(FrameworkCommon.lockPrx + lockKey, autoReleaseSeconds, overSeconds);
    //     }
    //     throw new LockReentrantApi.NoLockThrowException();
    // }

    // /**
    //  * 强制指定过期时间和报错时间
    //  */
    // static LockReentrantApi getLockAutoReleaseAndOverThrowExceptionByType(String lockKey, Long autoReleaseSeconds, Long overSeconds, String lockTypeStr){
    //
    //     String lockType = lockTypeStr;
    //     if(lockType == null){
    //         lockType = FrameworkCommon.lockImpl;
    //     }
    //
    //     if(lockType.equals(FrameworkCommon.lockImpl_jdk)){
    //         return LockReentrantJdk.getLockAutoReleaseAndOverThrowException(FrameworkCommon.lockPrx + lockKey, autoReleaseSeconds, overSeconds);
    //     }else if(lockType.equals(FrameworkCommon.lockImpl_redis)){
    //         return RedisLockReentrantDynamic.getLockAutoReleaseAndOverThrowException(FrameworkCommon.lockPrx + lockKey, autoReleaseSeconds, overSeconds);
    //     }
    //     throw new LockReentrantApi.NoLockThrowException();
    // }

    static LockList getLockList(LockReentrantApi ...locks){
        LockList lockList = new LockList();

        List<LockReentrantApi> RedisLockReentrantDynamics = Optional.ofNullable(locks).filter(o -> o.length > 0).map(o -> Arrays.asList(o)).orElse(new ArrayList<>());
        lockList.setLockList(RedisLockReentrantDynamics);

        return lockList;
    }

    @Data
    static class LockList {
        private List<LockReentrantApi> lockList;

        public void addLock(LockReentrantApi lock){
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

            for(LockReentrantApi lock : this.lockList){
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
            for(LockReentrantApi lock : this.lockList){
                lock.unlockIfSuccess();
            }
        }
    }
}
