package com.vict.framework.utils.lock;

import com.vict.framework.utils.lock.dao.MysqlLockMapper;
import com.vict.framework.utils.lock.entity.MysqlLock;
import com.vict.utils.SpringUtils;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.*;

@Slf4j
@Component
@Order(999)
public class LockMysql implements LockApi{

    // @Autowired
    // private MysqlLockMapper mysqlLockMapper;

    // 计划修复领取守护线程池
    public static ExecutorService lockDynamicDeamon = Executors.newFixedThreadPool(1);


    // 计划修复领取守护线程池
    public static ExecutorService expireLockDynamicDeamon = Executors.newFixedThreadPool(1);
    private static final Long EXPIRE_JOB_SECONDS = 60L; // 过期定时任务秒

    /**
     * 锁守护线程对象集合
     */
    public static Map<String, LockMysql> lockMap = new ConcurrentHashMap<String, LockMysql>();

    /**
     * 启动锁守护线程
     */
    @PostConstruct
    public void doExecute() {
        lockDynamicDeamon.submit(()->{
            while(true){
                try {
                    Thread.sleep(DEFAULT_OVER_SECONDS * 1000L / 4L);
                    Set<String> keySet = LockMysql.lockMap.keySet();
                    if(keySet == null || keySet.size() == 0){
                        continue;
                    }
                    for(String key : keySet){
                        try{
                            LockMysql lockMysql = LockMysql.lockMap.get(key);
                            if(lockMysql != null){
                                lockMysql.resetExpireTime();
                            }
                        }catch(Exception e){
                            log.error("mysqlLock延长锁时间异常", e);
                        }
                    }

                }catch(Exception e){
                    log.error("MysqlLock 睡眠异常", e);
                }
            }
        });

        // 定期清除过期锁
        expireLockDynamicDeamon.submit(()->{
            MysqlLockMapper mysqlLockMapper = SpringUtils.getBean(MysqlLockMapper.class);
            while(true){
                try{
                    Thread.sleep(EXPIRE_JOB_SECONDS * 1000L);
                    long now = System.currentTimeMillis();

                    mysqlLockMapper.deleteOverTime(now);

                }catch(Exception e){
                    log.error("MysqlLock 过期定时任务异常", e);
                }
            }
        });
    }


    /** 锁内容 */
    private String lockKey; // 锁key
    private long lockCreatedTimestamp; // 加锁时刻
    private boolean lockSuccessFlag = false; // 加锁成功标志

    private LockMysql(){}
    private LockMysql(String lockKey) {
        this.lockKey = lockKey;
    }

    @Override
    public String getLockKey() {
        return this.lockKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LockMysql that = (LockMysql) o;
        return lockKey.equals(that.lockKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lockKey);
    }

    private Long overTimeSeconds = null; // 强制指定过期时间 不指定则永久有效直到服务器挂掉
    private static final Long DEFAULT_OVER_SECONDS = 60L; // 系统默认过期时间时间秒

    private Long exceptionSeconds = 10L; // 报错时间 默认10秒

    public static LockMysql getLock(String lockKey){
        return new LockMysql(lockKey);
    }

    /** 强制指定报错时间 */
    public static LockMysql getLockOverThrowException(String lockKey, Long overSeconds){
        LockMysql LockMysql = new LockMysql(lockKey);
        if(overSeconds != null){
            LockMysql.exceptionSeconds = overSeconds; // 超时报错时间
        }
        return LockMysql;
    }

    /** 强制指定过期时间, 不指定则系统时间内永久有效, 直到系统崩溃 */
    public static LockMysql getLockAutoRelease(String lockKey, Long autoReleaseSeconds){
        LockMysql LockMysql = new LockMysql(lockKey);
        if(autoReleaseSeconds != null){
            LockMysql.overTimeSeconds = autoReleaseSeconds;
        }
        return LockMysql;
    }

    /** 强制指定过期时间和报错时间 */
    public static LockMysql getLockAutoReleaseAndOverThrowException(String lockKey, Long autoReleaseSeconds, Long overSeconds){
        LockMysql LockMysql = new LockMysql(lockKey);
        if(autoReleaseSeconds != null){
            LockMysql.overTimeSeconds = autoReleaseSeconds;
        }
        if(overSeconds != null){
            LockMysql.exceptionSeconds = overSeconds; // 超时报错时间
        }
        return LockMysql;
    }

    // 加锁线程池, 脱离主线程事务
    public static ExecutorService lockWorkPool = Executors.newFixedThreadPool(10);

    @SneakyThrows
    private boolean lock(String lockkey, Long ovetTimeSeconds){
        Future<Boolean> future = lockWorkPool.submit(() -> {
            MysqlLockMapper mysqlLockMapper = SpringUtils.getBean(MysqlLockMapper.class);

            long now = System.currentTimeMillis();
            MysqlLock mysqlLock = new MysqlLock();
            mysqlLock.setLockkey(lockkey);
            mysqlLock.setCreated(new Timestamp(now));
            mysqlLock.setOvertime(new Timestamp(now + (ovetTimeSeconds * 1000L)));
            try {
                int insert = mysqlLockMapper.insert(mysqlLock);
                if (insert > 0) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        });

        Boolean aBoolean = future.get(60, TimeUnit.SECONDS);
        return aBoolean;
    }

    @Override
    public void lockBlock() {
        if (
            lock(
                lockKey,
                (overTimeSeconds != null ? overTimeSeconds : DEFAULT_OVER_SECONDS)
            )
        ) {
            lockSuccess();
            return ;
        }
        long start = System.currentTimeMillis();
        long spendMillSecs = 0L;
        while (spendMillSecs < (exceptionSeconds * 1000L)) {
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (
                lock(
                    lockKey,
                    (overTimeSeconds != null ? overTimeSeconds : DEFAULT_OVER_SECONDS)
                )
            ) {
                lockSuccess();
                return;
            }
            spendMillSecs = System.currentTimeMillis() - start;
        }
        throw new LockThrowException();
    }

    @Override
    public boolean lockNotBlock(){
        if (
            lock(
                lockKey,
                (overTimeSeconds != null ? overTimeSeconds : DEFAULT_OVER_SECONDS)
            )
        ) {
            lockSuccess();
            return true;
        }else{
            return false;
        }
    }

    /** 加锁成功动作 */
    private void lockSuccess() {
        lockSuccessFlag = true;
        this.lockCreatedTimestamp = System.currentTimeMillis(); // 锁时间
        if (this.overTimeSeconds == null) { // 没有指定过期时间 则加入到守护线程中
            LockMysql.lockMap.put(this.lockKey, this);
        }
    }

    /** 释放锁 只有加锁成功才会释放锁 */
    @Override
    public void unlockIfSuccess() {
        if(lockSuccessFlag){
            try{
                LockMysql.lockMap.remove(this.lockKey);

                Future<?> future = lockWorkPool.submit(() -> {
                    MysqlLockMapper mysqlLockMapper = SpringUtils.getBean(MysqlLockMapper.class);
                    mysqlLockMapper.deleteById(lockKey);
                });
                future.get(60L, TimeUnit.SECONDS);
            }catch(Exception e){
                log.error("MysqlLock,锁释放异常", e);
            }
        }
    }

    public static void unlockIfSuccess(LockApi lock){
        Optional.ofNullable(lock).ifPresent(o-> o.unlockIfSuccess());
    }

    /** 锁自身时间延长 只有加锁成功才会延长并且没有强制指定过期时间 */
    public void resetExpireTime() {
        try{
            if(lockSuccessFlag && overTimeSeconds == null){
                long now = System.currentTimeMillis();
                if((now - lockCreatedTimestamp) > (DEFAULT_OVER_SECONDS * 1000L / 2L)){
                    MysqlLockMapper mysqlLockMapper = SpringUtils.getBean(MysqlLockMapper.class);
                    mysqlLockMapper.expire(lockKey, new Timestamp(now + DEFAULT_OVER_SECONDS * 1000L));
                }
            }
        }catch(Exception e){
            log.error("重置锁过期时间一长", e);
        }
    }

    public static LockMysqlList getLockList(LockMysql ...locks){
        LockMysqlList redisLockDynamicList = new LockMysqlList();

        List<LockMysql> lockMysqls = Optional.ofNullable(locks).filter(o -> o.length > 0).map(o -> Arrays.asList(o)).orElse(new ArrayList<>());
        redisLockDynamicList.setLockList(lockMysqls);

        return redisLockDynamicList;
    }

    @Data
    public static class LockMysqlList{

        private List<LockMysql> lockList;

        public void addLock(LockMysql lock){
            Optional.ofNullable(this.lockList).ifPresent(o-> o.add(lock));
        }

        public void lockBlock(){
            if(this.lockList == null || this.lockList.size() <= 0){
                return;
            }

            this.lockList.sort((o1, o2) -> {
                if(o1.lockKey.compareTo(o2.lockKey) < 0){
                    return -1;
                }else if(o1.lockKey.compareTo(o2.lockKey) > 0){
                    return 1;
                }else{
                    return 0;
                }
            });

            for(LockMysql lock : this.lockList){
                lock.lockBlock();
            }
        }

        public void unlockIfSuccess() {
            if(this.lockList == null || this.lockList.size() <= 0){
                return;
            }
            this.lockList.sort((o1, o2) -> {
                if(o1.lockKey.compareTo(o2.lockKey) < 0){
                    return -1;
                }else if(o1.lockKey.compareTo(o2.lockKey) > 0){
                    return 1;
                }else{
                    return 0;
                }
            });
            for(LockMysql lock : this.lockList){
                lock.unlockIfSuccess();
            }
        }

    }
}
