package com.vict.framework.utils.count;

import com.vict.framework.keyvalue.entity.KeyValue;
import com.vict.framework.FrameworkCommon;
import com.vict.framework.utils.count.core.CountApi;
import com.vict.framework.utils.count.core.CountJdk;
import com.vict.framework.utils.count.core.CountRedis;
import com.vict.framework.utils.lock.LockApi;
import com.vict.utils.KeyValueUtil;
import com.vict.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.*;

@Slf4j
@Component
public class CountUtil {

    private static final String synclock_key_pre = "normalCountSyncLock_"; // 同步lock锁前缀
    private static final String lock_key_pre = "normalCountLock_"; // lock锁前缀
    private static final String key_pre = "normalCount_"; // 计数存储前缀
    private static final String keyValue_key_pre = "keyValueNormalCount_"; // keyValue计数存储前缀

    private static Integer keyValueOverPlus = 10; // keyValue预超越数
    private static ConcurrentHashMap<String, String> allKeySet = new ConcurrentHashMap<String, String>(); // 全部key
    private static ExecutorService NormalCountDaemonThreadPool = new ThreadPoolExecutor(1, 1, 99999, TimeUnit.DAYS, new ArrayBlockingQueue<>(1));
    private static HashMap<String, ExecutorService> syncNormalCountWorkThreadPoolMap = new HashMap<String, ExecutorService>();
    private static Integer syncNormalCountWorkSleepTime = 10000; // 同步线程job频率
    private static Integer DaemonSleepTime = 15000; // 同步当前keySetJob频率

    private static HashMap<String, ExecutorService> syncActionNormalCountWorkThreadPoolMap = new HashMap<String, ExecutorService>();

    private static CountApi countApi;

    @PostConstruct
    private void init(){

        if(FrameworkCommon.countImpl.equals(FrameworkCommon.countImpl_redis)){
            CountUtil.countApi = SpringUtils.getBean(CountRedis.class);
        }else if(FrameworkCommon.countImpl.equals(FrameworkCommon.countImpl_jdk)){
            CountUtil.countApi = SpringUtils.getBean(CountJdk.class);
        }else{
            throw new RuntimeException("countImpl配置错误");
        }

        NormalCountDaemonThreadPool.execute(() -> {
            while(true){
                try {
                    Thread.sleep(DaemonSleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                LockApi lock = LockApi.getLockOverThrowExceptionByType("NormalCountDaemonLock", 100L, FrameworkCommon.lockImpl_jdk);

                boolean b = lock.lockNotBlock();
                if(b){
                    try{
                        for(String key : allKeySet.keySet()){
                            startSyncWorkThread(key, allKeySet.get(key));
                            initSyncActionWorkThread(key, allKeySet.get(key));
                        }
                    }finally{
                        Optional.ofNullable(lock).ifPresent(o-> o.unlockIfSuccess());
                    }
                }
            }
        });
    }

    private static void initSyncActionWorkThread(String key, String keyValueKeyStr){
        ExecutorService executorService = syncActionNormalCountWorkThreadPoolMap.get(key);
        if(executorService != null){
            return;
        }

        executorService = new ThreadPoolExecutor(1, 1, 99999, TimeUnit.DAYS, new LinkedBlockingDeque<>(1), new ThreadPoolExecutor.DiscardPolicy());
        ExecutorService executorServiceExist = syncActionNormalCountWorkThreadPoolMap.putIfAbsent(key, executorService);
        if(executorServiceExist != null){
            // 已经存在线程池
            try{
                executorService.shutdown();
                executorService.shutdownNow();
            }catch(Exception e){
                log.error("", e);
            }
        }
    }

    /**
     * 启动守护线程
     */
    private void startSyncWorkThread(String key, String keyValueKeyStr){
        ExecutorService executorService = syncNormalCountWorkThreadPoolMap.get(key);
        if(executorService != null){
            return;
        }
        executorService = new ThreadPoolExecutor(1, 1, 99999, TimeUnit.DAYS, new ArrayBlockingQueue<>(1));
        ExecutorService executorServiceExist = syncNormalCountWorkThreadPoolMap.putIfAbsent(key, executorService);
        if(executorServiceExist != null){
            // 已经存在线程池
            try{
                executorService.shutdown();
                executorService.shutdownNow();
            }catch(Exception e){
                log.error("", e);
            }
        }else{
            executorService.execute(() -> {
                while(true){
                    try {
                        Thread.sleep(syncNormalCountWorkSleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    syncWorkThreadMain(key, keyValueKeyStr);
                }
            });
        }
    }

    private static void syncWorkThreadMain(String key, String keyValueKeyStr){
        LockApi lock = LockApi.getLockOverThrowException(synclock_key_pre + key, 100L);
        boolean b = lock.lockNotBlock();
        if(b){
            try{

                Long num = CountUtil.countApi.getNumMain(key);
                Long keyValueNum = getKeyValueNumMain(keyValueKeyStr);
                if(num != null && keyValueNum != null){
                    if(num > keyValueNum - keyValueOverPlus){
                        setKeyValueStockMain(num + keyValueOverPlus, keyValueKeyStr);
                    }
                }
            }finally{
                Optional.ofNullable(lock).ifPresent(o-> o.unlockIfSuccess());
            }
        }
    }

    /**
     * Key
     */
    private static String getKeyStr(String... key){
        String keys = "";
        if(key != null){
            for(String itemKey : key){
                keys += itemKey;
                keys += "_";
            }
        }
        String keyStr = key_pre + "_" + keys;
        String keyValueKeyStr = keyValue_key_pre + "_" + keys;
        allKeySet.put(keyStr, keyValueKeyStr);
        return keyStr;
    }

    /**
     * keyValueKey
     */
    private static String getKeyValueKeyStr(String... key){
        String keys = "";
        if(key != null){
            for(String itemKey : key){
                keys += itemKey;
                keys += "_";
            }
        }
        String keyStr = keyValue_key_pre + "_" + keys;
        return keyStr;
    }

    /**
     * 获取库存
     */
    private static Long getNum(String... key){
        String keyStr = getKeyStr(key);

        return CountUtil.countApi.getNumMain(keyStr);
    }



    /**
     * 获取keyValue库存
     */
    private static Long getKeyValueNum(String... key){
        String keyValueKeyStr = getKeyValueKeyStr(key);

        return getKeyValueNumMain(keyValueKeyStr);
    }

    private static Long getKeyValueNumMain(String keyValueKeyStr){
        KeyValue valueByKey = KeyValueUtil.getValueByKey(keyValueKeyStr);
        String value = valueByKey.getValue1();
        if(value == null || value.trim().equals("")){
            return null;
        }else{
            return Long.parseLong(value);
        }
    }

    /**
     * 设置keyValue库存
     */
    private static void setKeyValueStock(Long num, String... key){
        String keyStr = getKeyValueKeyStr(key);

        setKeyValueStockMain(num, keyStr);
    }

    private static void setKeyValueStockMain(Long num, String keyStr){
        KeyValue keyValue = new KeyValue();
        keyValue.setKey(keyStr);
        keyValue.setValue1(String.valueOf(num));
        keyValue.setDesc("normalCount计数器 会超越计数器一部分做数据预加 防止数据被清空");
        keyValue.setHiddenFlag(1);
        KeyValueUtil.addOrUpdateByKey(keyValue);
    }


    /**
     * 重置库存
     */
    private static long reloadWithLock(String... key){
        String keys = "";
        if(key != null){
            for(String itemKey : key){
                keys += itemKey;
                keys += "_";
            }
        }
        LockApi lock = LockApi.getLock(lock_key_pre + keys);
        try{
            lock.lockBlock();

            String keyStr = key_pre + "_" + keys;
            String keyValueKeyStr = keyValue_key_pre + "_" + keys;
            initSyncActionWorkThread(keyStr, keyValueKeyStr);

            Long num = getNum(key);
            if(num == null){
                return reload(key);
            }else{
                return num;
            }

        } catch (Exception e) {
            log.error("normalCount_reload异常, KeyStr={}", getKeyStr(key), e);
            throw new RuntimeException("normalCount_reload异常");
        } finally{
            Optional.ofNullable(lock).ifPresent(o-> o.unlockIfSuccess());
        }
    }

    /**
     * 重置库存
     */
    private static long reload(String... key){
        String keyStr = getKeyStr(key);
        countApi.delete(keyStr);
        Long dbNum = getKeyValueNum(key); // db数据

        if(dbNum == null){
            setKeyValueStock(0L/* + keyValueOverPlus*/, key);
            dbNum = 0L/* + keyValueOverPlus*/;
        }
        countApi.increment(keyStr, dbNum);
        return dbNum.longValue();
    }


    /**
     * 获取库存数量
     */
    public static long selectNum(String... key){
        Long num = getNum(key);
        if(num == null){
            return reloadWithLock(key);
        }else{
            return num;
        }
    }

    /**
     * 自增库存
     */
    public static long nextNum(String... key){
        Long num = getNum(key);
        if(num == null){
            reloadWithLock(key);
        }
        String keyStr = getKeyStr(key);
        String keyValueKeyStr = getKeyValueKeyStr(key);
        Long increment = countApi.nextNumMain(keyStr);

        Optional.ofNullable(syncActionNormalCountWorkThreadPoolMap).map(o-> o.get(keyStr)).ifPresent(o-> o.submit(()->{
            syncWorkThreadMain(keyStr, keyValueKeyStr);
        }));

        return increment;
    }
}
