package com.vict.framework.task.core.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.vict.framework.FrameworkCommon;
import com.vict.framework.bean.UserContext;
import com.vict.framework.task.annotation.TaskConsumerConfiguration;
import com.vict.framework.utils.IdUtils;
import com.vict.framework.utils.lock.LockApi;
import com.vict.framework.task.bean.dto.IdTimeDTO;
import com.vict.framework.task.bean.dto.Task;
import com.vict.framework.task.bean.dto.TaskConsumeResult;
import com.vict.framework.task.bean.entity.TaskExecuteInstance;
import com.vict.framework.task.core.TaskExecute;
import com.vict.framework.task.core.TaskService;
import com.vict.framework.task.dao.TaskExecuteInstanceDao;
import com.vict.utils.ObjectUtil;
import com.vict.utils.ThreadUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.PostConstruct;
import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.*;

/**
 * version 1.2
 * 重试机制
 */
@Slf4j
@Component
public class TaskExecuteProcessor {
    public static ExecutorService TestExecuteProcessorThreadPoolDeamon = null;
    public static ExecutorService[] TestExecuteProcessorThreadPoolWork = null;
    public static ExecutorService TestExecuteProcessorThreadPoolAction = new ThreadPoolExecutor(100, 200, 99999, TimeUnit.DAYS, new ArrayBlockingQueue<>(10000));

    public static ExecutorService syncThreadPool = new ThreadPoolExecutor(100, 200, 99999, TimeUnit.DAYS, new ArrayBlockingQueue<>(10000));

    public static ConcurrentHashMap<IdTimeDTO, IdTimeDTO> idTimeMap = new ConcurrentHashMap<IdTimeDTO, IdTimeDTO>();

    @Autowired(required = false)
    List<TaskExecute> taskExecutes;

    @Autowired
    TaskExecuteInstanceDao taskExecuteInstanceDao;

    @Autowired
    TaskService taskService;

    @Autowired
    TaskExecuteProcessor taskExecuteProcessor;

    @PostConstruct
    public void init() {
        log.info("任务执行器, 当前注册执行器数量:{}", Optional.ofNullable(taskExecutes).map(List::size).orElse(0));

        Integer taskNum = Optional.ofNullable(taskExecutes).map(List::size).orElse(0);
        if(taskNum == 0){
            return;
        }

        // 执行器守护线程
        TaskExecuteProcessor.TestExecuteProcessorThreadPoolDeamon = new ThreadPoolExecutor(taskNum, taskNum, 99999, TimeUnit.DAYS, new ArrayBlockingQueue<>(taskNum));
        // 执行器工作线程
        TestExecuteProcessorThreadPoolWork = (ExecutorService[]) Array.newInstance(ExecutorService.class, taskNum);
        for(int i = 0; i < taskExecutes.size() ; i++){
            String type = Optional.ofNullable(taskExecutes.get(i)).map(o -> o.getClass())
                    .map(o -> o.getAnnotation(TaskConsumerConfiguration.class))
                    .map(TaskConsumerConfiguration::type).orElseThrow(() -> new RuntimeException("任务执行器TaskConsumerConfiguration->type不可为空"));
            int sameTimeWorkNum = Optional.ofNullable(taskExecutes.get(i)).map(o -> o.getClass())
                    .map(o -> o.getAnnotation(TaskConsumerConfiguration.class))
                    .map(TaskConsumerConfiguration::sameTimeWorkNum).get();

            TestExecuteProcessorThreadPoolWork[i] = new ThreadPoolExecutor(sameTimeWorkNum, sameTimeWorkNum, 99999, TimeUnit.DAYS, new ArrayBlockingQueue<>(100000));
        }
        // 启动守护线程
        for(int i = 0; i < taskExecutes.size() ; i++){
            TaskExecute taskExecute = taskExecutes.get(i);
            int finalI = i;
            TaskExecuteProcessor.TestExecuteProcessorThreadPoolDeamon.submit(()-> {
                while(true){
                    try{
                        Thread.sleep(FrameworkCommon.taskScanTime);
                        String type = Optional.ofNullable(taskExecute).map(o -> o.getClass())
                                .map(o -> o.getAnnotation(TaskConsumerConfiguration.class))
                                .map(TaskConsumerConfiguration::type).orElseThrow(() -> new RuntimeException("任务执行器TaskConsumerConfiguration->type不可为空"));
                        Integer sameTimeWorkNum = Optional.of(taskExecute).map(o -> o.getClass())
                                .map(o -> o.getAnnotation(TaskConsumerConfiguration.class))
                                .map(TaskConsumerConfiguration::sameTimeWorkNum).orElse(null);
                        int[] failRetryTimeSeconds = Optional.ofNullable(taskExecute).map(o -> o.getClass())
                                .map(o -> o.getAnnotation(TaskConsumerConfiguration.class))
                                .map(TaskConsumerConfiguration::failRetryTimeSeconds).get();
                        // log.info("任务执行器开始扫描任务, type={}", type);

                        startTaskExecuteProcessor(finalI, taskExecute, type, sameTimeWorkNum, failRetryTimeSeconds);
                    }catch(Exception e){
                        log.error("任务执行器, 执行异常", e);
                    }
                }
            });
        }
    }


    /**
     * 直接触发
     */
    public void startTaskExecuteProcessor(String type){
        Integer index = getTaskExecuteByType(type);
        if(index == null){
            return;
        }
        TestExecuteProcessorThreadPoolAction.submit(()-> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) { }

            Integer sameTimeWorkNum = Optional.ofNullable(taskExecutes.get(index)).map(o -> o.getClass())
                    .map(o -> o.getAnnotation(TaskConsumerConfiguration.class))
                    .map(TaskConsumerConfiguration::sameTimeWorkNum).get();
            int[] failRetryTimeSeconds = Optional.ofNullable(taskExecutes.get(index)).map(o -> o.getClass())
                    .map(o -> o.getAnnotation(TaskConsumerConfiguration.class))
                    .map(TaskConsumerConfiguration::failRetryTimeSeconds).get();

            startTaskExecuteProcessor(index, taskExecutes.get(index), type, sameTimeWorkNum, failRetryTimeSeconds);
            return null;
        });
    }

    /**
     * 直接触发同步
     */
    public void startTaskExecuteProcessorSync(String type, TaskExecuteInstance taskExecuteInstance){
        Integer index = getTaskExecuteByType(type);
        if(index == null){
            return;
        }

        TaskExecute taskExecute = taskExecutes.get(index);

        int[] failRetryTimeSeconds = Optional.ofNullable(taskExecutes.get(index)).map(o -> o.getClass())
                .map(o -> o.getAnnotation(TaskConsumerConfiguration.class))
                .map(TaskConsumerConfiguration::failRetryTimeSeconds).get();

        String id = taskExecuteInstance.getTaskExecuteInstanceID();
        LockApi lockMain = LockApi.getLock("TaskLockMain" + "-id-" + id);

        if(lockMain.lockNotBlock()){
            try{
                startTaskExecuteProcessorOneSync(taskExecute, taskExecuteInstance, failRetryTimeSeconds);
            }finally{
                // 执行完毕, 锁进行释放
                lockMain.unlockIfSuccess();
            }
        }else{
            log.info("已经有任务正在进行, type={}, taskExecuteInstance={}", type, ObjectUtil.toString(taskExecuteInstance));
        }
    }

    private Integer getTaskExecuteByType(String type){
        for(int i = 0; i < taskExecutes.size() ; i++){
            TaskExecute taskExecute = taskExecutes.get(i);
            Boolean b = Optional.ofNullable(taskExecute).map(o -> o.getClass())
                    .map(o -> o.getAnnotation(TaskConsumerConfiguration.class))
                    .map(TaskConsumerConfiguration::type).map(o -> o.equals(type)).orElse(false);
            if(b){
                return i;
            }
        }
        return null;
    }

    public void startTaskExecuteProcessor(int index, TaskExecute taskExecute, String type, Integer sameTimeWorkNum, int[] failRetryTimeSeconds){
        // 查找任务
        List<TaskExecuteInstance> taskExecuteInstances = selectTaskList(type, sameTimeWorkNum);
        if(taskExecuteInstances == null || taskExecuteInstances.size() == 0){
            log.debug("任务扫描无数据 now={}", System.currentTimeMillis());
            return;
        }

        for(TaskExecuteInstance taskExecuteInstance : taskExecuteInstances){
            TestExecuteProcessorThreadPoolWork[index].submit(()-> {
                startTaskExecuteProcessorOne(taskExecuteInstance, taskExecute, failRetryTimeSeconds);
                return null;
            });
        }
    }

    private void startTaskExecuteProcessorOne(TaskExecuteInstance taskExecuteInstance, TaskExecute taskExecute, int[] failRetryTimeSeconds){
        IdTimeDTO idTimeDTO = new IdTimeDTO();
        idTimeDTO.setId(taskExecuteInstance.getTaskExecuteInstanceID());
        idTimeDTO.setActionTime(taskExecuteInstance.getActionTime().getTime());
        idTimeDTO.setOverTimestamp(System.currentTimeMillis() + 1000 * 60 * 5);

        IdTimeDTO idTimeDTOOld = idTimeMap.putIfAbsent(idTimeDTO, idTimeDTO);
        if(idTimeDTOOld == null){ // 添加成功
            // 任务发起成功
            log.info("任务发起成功, id={}", taskExecuteInstance.getTaskExecuteInstanceID());
            startTaskExecuteProcessorOneMain(taskExecuteInstance, taskExecute, failRetryTimeSeconds);
        }else{
            // 已经存在
            long now = System.currentTimeMillis();
            if(idTimeDTOOld.getOverTimestamp() <= now){
                // 过期
                idTimeMap.remove(idTimeDTOOld); // 移除旧任务
                idTimeDTOOld = idTimeMap.putIfAbsent(idTimeDTO, idTimeDTO); // 添加新任务
                if(idTimeDTOOld == null){ // 添加成功
                    log.info("任务过期 再次发起成功, id={}", taskExecuteInstance.getTaskExecuteInstanceID());
                    startTaskExecuteProcessorOneMain(taskExecuteInstance, taskExecute, failRetryTimeSeconds);
                }else{
                    log.info("任务过期 再次发起失败, 无需处理, id={}", taskExecuteInstance.getTaskExecuteInstanceID());
                }
            }else{
                // 未过期
                log.info("任务未过期 无需发起, id={}", taskExecuteInstance.getTaskExecuteInstanceID());
            }
        }
    }

    private void startTaskExecuteProcessorOneMain(TaskExecuteInstance taskExecuteInstance, TaskExecute taskExecute, int[] failRetryTimeSeconds){
        Date date = new Date(taskExecuteInstance.getActionTime().getTime());
        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override
            public void run() {
                String id = taskExecuteInstance.getTaskExecuteInstanceID();
                LockApi lockMain = LockApi.getLock("taskLockMain" + "-id-" + id);

                if(lockMain.lockNotBlock()){ // 同时只能有一条记录
                    try{
                        TaskExecuteInstance taskExecuteInstanceNow = taskExecuteInstanceDao.selectById(id);
                        if(taskExecuteInstanceNow.getStatus() == TaskExecuteInstance.status_finish
                                || taskExecuteInstanceNow.getStatus() == TaskExecuteInstance.status_fail){
                            return;
                        }

                        startTaskExecuteProcessorOneSync(taskExecute, taskExecuteInstance, failRetryTimeSeconds);
                    }finally{
                        lockMain.unlockIfSuccess();

                        // 移除本地缓存
                        IdTimeDTO idTimeDTO = new IdTimeDTO();
                        idTimeDTO.setId(taskExecuteInstance.getTaskExecuteInstanceID());
                        idTimeDTO.setActionTime(taskExecuteInstance.getActionTime().getTime());
                        idTimeMap.remove(idTimeDTO);
                    }
                }else{
                    log.info("已经有任务正在进行, taskExecuteInstance={}", ObjectUtil.toString(taskExecuteInstance));
                }
            }
        }, date);
    }

    private void startTaskExecuteProcessorOneSync(TaskExecute taskExecute,
                                                  TaskExecuteInstance taskExecuteInstance,
                                                  int[] failRetryTimeSeconds){
        TaskExecuteInstance taskExecuteInstanceNext = null;
        try{
            // 执行任务
            taskExecuteInstanceNext = taskOneInstanceStart(taskExecute, taskExecuteInstance, failRetryTimeSeconds);
        }catch(Exception e){
            log.error("taskOneInstanceStart异常, tTaskExecuteInstanceID={}", taskExecuteInstance.getTaskExecuteInstanceID(), e);
        }finally{
            if(taskExecuteInstanceNext != null){ // 下一次触发
                if(taskExecuteInstanceNext.getActionTime().getTime() <= (System.currentTimeMillis() + FrameworkCommon.taskScanTime_extend)){
                    // this.startTaskExecuteProcessor(type);
                    startTaskExecuteProcessorOne(taskExecuteInstanceNext, taskExecute, failRetryTimeSeconds);
                }
            }
        }
    }

    @SneakyThrows
    private TaskExecuteInstance taskOneInstanceStart(TaskExecute taskExecute, TaskExecuteInstance taskExecuteInstance, int[] failRetryTimeSeconds) {

        // 用任务自身的过期时间替代配置的过期时间
        List<Integer> failRetryTimeSeconds2 = Optional.ofNullable(taskExecuteInstance).map(o -> o.getFailRetryTimeSeconds()).map(o -> o.trim())
                .filter(o -> !o.equals("")).map(o -> JSONArray.parseArray(o, Integer.class))
                .orElse(null);
        if(failRetryTimeSeconds2 != null && failRetryTimeSeconds2.size() > 0){
            int[] ints = new int[failRetryTimeSeconds2.size()];
            for(int i = 0; i < failRetryTimeSeconds2.size(); i++){
                ints[i] = failRetryTimeSeconds2.get(i);
            }
            failRetryTimeSeconds = ints;
        }

        TaskExecuteInstance returnTaskExecuteInstance = null;
        try{
            // 初始化Token
            ThreadUtil.putThreadVariable("userContext", Optional.ofNullable(taskExecuteInstance).map(o-> o.getUserContext()).map(o-> JSONObject.parseObject(o, UserContext.class)).orElse(null));

            String executeJSON = taskExecuteInstance.getExecuteJSON();
            Object parse = JSONObject.parse(executeJSON);
            Object t = parse;

            Task task = new Task();
            task.setExecuteCount(taskExecuteInstance.getExecuteCount());
            task.setTaskExecuteInstanceID(taskExecuteInstance.getTaskExecuteInstanceID());
            task.setTaskService(taskService);
            task.setActionTime(taskExecuteInstance.getActionTime());

            TaskConsumeResult taskConsumeResult = new TaskConsumeResult();
            taskConsumeResult.setResult(TaskConsumeResult.result_reconsumeLaterConsumer);
            try{
                TaskConsumeResult taskConsumeResultTem = taskExecute.doConsume(task, t);
                if(taskConsumeResultTem != null){
                    taskConsumeResult = taskConsumeResultTem;
                }
            }catch(Exception e){
                log.error("任务执行器, 一个任务执行异常 任务id={}, t={}", taskExecuteInstance.getTaskExecuteInstanceID(),
                        Optional.ofNullable(t).map(o -> o.getClass()).map(o-> o.getName()).orElse(null),
                        e);
            }

            if(taskConsumeResult.getResult() == TaskConsumeResult.result_commitConsumer){
                // 任务执行成功
            }else if(taskConsumeResult.getResult() == TaskConsumeResult.result_failConsumer){
                // 任务执行失败
                taskExecuteInstanceDao.updateStatusById(taskExecuteInstance.getTaskExecuteInstanceID(), TaskExecuteInstance.status_fail);
            }else if(taskConsumeResult.getResult() == TaskConsumeResult.result_reconsumeLaterConsumer){
                // 任务执行失败 重试任务
                if(failRetryTimeSeconds == null || failRetryTimeSeconds.length == 0){ // 没有重试机制使用默认重试机制
                    Integer executeCount = taskExecuteInstance.getExecuteCount();
                    if(executeCount > 3){
                        taskExecuteInstanceDao.updateStatusById(taskExecuteInstance.getTaskExecuteInstanceID(), TaskExecuteInstance.status_fail);
                    }else{
                        taskExecuteInstanceDao.updateStatusById(taskExecuteInstance.getTaskExecuteInstanceID(), TaskExecuteInstance.status_run);
                        taskExecuteInstanceDao.updateExecuteCount(taskExecuteInstance.getTaskExecuteInstanceID(), executeCount + 1);
                    }
                }else{
                    Integer executeCount = taskExecuteInstance.getExecuteCount();
                    Integer failRetryTimeSecond = Optional.ofNullable(failRetryTimeSeconds).filter(o-> o.length > executeCount).map(o-> o[executeCount]).orElse(null);
                    if(failRetryTimeSecond == null){
                        // 终止
                        taskExecuteInstanceDao.updateStatusById(taskExecuteInstance.getTaskExecuteInstanceID(), TaskExecuteInstance.status_fail);
                    }else{
                        long nowActionTime = taskExecuteInstance.getActionTime().getTime() + failRetryTimeSecond * 1000L;
                        taskExecuteInstance.setActionTime(new Timestamp(nowActionTime));
                        taskExecuteInstance.setStatus(TaskExecuteInstance.status_run);
                        taskExecuteInstance.setExecuteCount(executeCount + 1);
                        taskExecuteInstanceDao.updateById(taskExecuteInstance);

                        returnTaskExecuteInstance = taskExecuteInstance;
                    }
                }

            }

            return returnTaskExecuteInstance;
        }catch(Exception e){
            throw e;
        }
    }

    /**
     * 扫描可执行任务
     * @param type 任务类型
     * @param sameTimeWorkNum 每个社区同一时间执行数量
     */
    private List<TaskExecuteInstance> selectTaskList(String type, Integer sameTimeWorkNum){
        LockApi lock = LockApi.getLock("loaclTaskSelectTasklock" + "-type-" + type);
        try{
            lock.lockBlock();
            return selectTaskListMain(type, sameTimeWorkNum);
        } catch (Exception e) {
            log.error("任务执行器扫描任务等待超时" + "-type-" + type, e);
        } finally{
            Optional.ofNullable(lock).ifPresent(o-> o.unlockIfSuccess());
        }
        return null;
    }

    @SneakyThrows
    private List<TaskExecuteInstance> selectTaskListMain(String type, Integer sameTimeWorkNum) {
        try{
            long now = System.currentTimeMillis();
            List<TaskExecuteInstance> taskExecuteInstances = taskExecuteInstanceDao.selectGroup(
                    FrameworkCommon.springApplicationName,
                    FrameworkCommon.taskSelfFlag ? FrameworkCommon.serverId: null,
                    type,
                    sameTimeWorkNum,
                    new Timestamp(now + FrameworkCommon.taskScanTime_extend));
            if(taskExecuteInstances == null || taskExecuteInstances.size() == 0){
                return null;
            }
            // 取全部id
            List<String> idSqls = new ArrayList<>();
            for(TaskExecuteInstance oneCid : taskExecuteInstances){
                String[] taskExecuteInstanceIDArr = Optional.ofNullable(oneCid).map(o-> o.getTaskExecuteInstanceIDList()).map(o-> o.trim())
                        .filter(o-> !o.equals("")).map(o-> o.split(",")).orElseGet(()-> new String[0]);
                for(String id : taskExecuteInstanceIDArr){
                    idSqls.add(id);
                }
            }

            List<TaskExecuteInstance> taskExecuteInstancesAll = new ArrayList<TaskExecuteInstance>();
            if(idSqls != null && idSqls.size() != 0){
                taskExecuteInstancesAll = taskExecuteInstanceDao.selectBatchIds(idSqls);
            }

            return taskExecuteInstancesAll;
        }catch(Exception e){
            log.error("任务执行器-扫描任务异常", e);
            return null;
        }
    }

    /**
     * 发起一个任务独立事务
     */
    @SneakyThrows
    public <T> void sendTask(String type,
                             T t,
                             Timestamp actionTime,
                             int[] failRetryTimeSeconds){
        long now = System.currentTimeMillis();

        String json = JSONObject.toJSONString(t, SerializerFeature.WriteClassName);
        TaskExecuteInstance taskExecuteInstance = new TaskExecuteInstance();
        taskExecuteInstance.setTaskExecuteInstanceID(IdUtils.snowflakeId() + "");
        taskExecuteInstance.setStatus(TaskExecuteInstance.status_await);
        taskExecuteInstance.setExecuteCount(0);
        taskExecuteInstance.setExecuteJSON(json);
        taskExecuteInstance.setServerName(FrameworkCommon.springApplicationName);

        String failRetryTimeSecondsStr = Optional.ofNullable(failRetryTimeSeconds).filter(o -> o.length > 0).map(o -> JSONArray.toJSONString(o)).orElse(null);
        taskExecuteInstance.setFailRetryTimeSeconds(failRetryTimeSecondsStr);

        if(FrameworkCommon.taskSelfFlag){
            taskExecuteInstance.setServerId(FrameworkCommon.serverId);
        }

        taskExecuteInstance.setType(type);
        taskExecuteInstance.setActionTime(actionTime);

        taskExecuteInstance.setUserContext(
            Optional.ofNullable(ThreadUtil.getThreadVariable("userContext"))
                .map(o-> (UserContext)o)// 上下文对象
                .map(o-> JSONObject.toJSONString(o, SerializerFeature.WriteClassName)).orElse(null));

        taskExecuteInstanceDao.insert(taskExecuteInstance);

        log.debug("任务插入数据 actionTime={}", actionTime.getTime());

        // 如果可以开始则直接执行
        if(actionTime.getTime() <= (now + FrameworkCommon.taskScanTime_extend)){

            boolean inTransaction = TransactionSynchronizationManager.isActualTransactionActive();
            //非事务状态，直接执行，不做任何保证。
            if (!inTransaction) {
                taskExecuteProcessor.startTaskExecuteProcessor(type);
            }else{
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                    @SneakyThrows
                    @Override
                    public void afterCommit() {
                    //事务后执行
                    taskExecuteProcessor.startTaskExecuteProcessor(type);
                    }
                });
            }
        }
        // if(async){ // 异步执行
        // }else{ // 同步执行
            // boolean inTransaction = TransactionSynchronizationManager.isActualTransactionActive();
            // //非事务状态，直接执行，不做任何保证。
            // if (!inTransaction) {
            //     Map<String, AtomicInteger> countMap = LockReentrantApi.getCountMap(); // 重试锁
            //     Future<?> future = syncThreadPool.submit(() -> {
            //         LockReentrantApi.setCountMap(countMap);
            //         taskExecuteProcessor.startTaskExecuteProcessorSync(type, taskExecuteInstance);
            //     });
            //     future.get(60L, TimeUnit.SECONDS);
            // }else{
            //     Map<String, AtomicInteger> countMap = LockReentrantApi.getCountMap(); // 重试锁
            //     TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            //         @SneakyThrows
            //         @Override
            //         public void afterCommit() {
            //             //事务后执行
            //             Future<?> future = syncThreadPool.submit(() -> {
            //                 LockReentrantApi.setCountMap(countMap);
            //                 taskExecuteProcessor.startTaskExecuteProcessorSync(type, taskExecuteInstance);
            //             });
            //             future.get(60L, TimeUnit.SECONDS);
            //         }
            //     });
            // }
        // }
    }

    /**
     * 删除任务 独立事务
     * @param taskExecuteInstanceID
     */
    @SneakyThrows
    public void taskDelete(String taskExecuteInstanceID) {

        try{
            long now = System.currentTimeMillis();
            TaskExecuteInstance taskExecuteInstance = taskExecuteInstanceDao.selectById(taskExecuteInstanceID);
            if (taskExecuteInstance.getActionTime().getTime() <= now + 30L * 1000L) {
                // 任务已经开始,或马上开始, 无法更改
                log.error("任务已经开始或将要开始, 无法终止");
                throw new RuntimeException("任务已经开始无法终止");
            }
            taskExecuteInstanceDao.deleteById(taskExecuteInstanceID);
        }catch(Exception e){
            throw e;
        }
    }
}
