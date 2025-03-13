package com.vict.framework.job.core;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.vict.framework.job.entity.Job;
import com.vict.framework.job.entity.JobHistory;
import com.vict.framework.job.mapper.JobHistoryMapper;
import com.vict.framework.job.mapper.JobMapper;
import com.vict.framework.keyvalue.entity.KeyValue;
import com.vict.framework.utils.ExceptionUtils;
import com.vict.framework.utils.IdUtils;
import com.vict.utils.KeyValueUtil;
import com.vict.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JobService {
    private static ThreadPoolTaskScheduler threadPoolTaskScheduler;

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private JobHistoryMapper jobHistoryMapper;

    public static List<String> keys = new ArrayList<>();
    private static HashMap<String, ScheduledFuture> taskMap = new HashMap<>();
    private static HashMap<String, Job> jobs = new HashMap<>();

    @PostConstruct
    public void init(){

        threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(50);
        threadPoolTaskScheduler.initialize();

        List<Job> allStartupJobs = jobMapper.selectList(new LambdaQueryWrapper<Job>().eq(Job::getStatus, Job.Status.enable));
        if(allStartupJobs == null || allStartupJobs.size() == 0){
            return;
        }
        for(Job job : Optional.ofNullable(allStartupJobs).orElse(new ArrayList<>())){
            startRun(job);
        }
    }

    public void startRun(Job job){
        try{
            String key = Optional.ofNullable(job)
                    .map(o-> o.getId()).map(o-> o.toString())
                    .orElseThrow(() -> new RuntimeException("定时任务启动失败, 未配置MyJobApi"));

            String cron = job.getCron();

            ScheduledFuture<?> schedule = threadPoolTaskScheduler.schedule(() -> {
                run(job);
            }, new CronTrigger(cron));
            keys.add(key);
            jobs.put(key, job);
            taskMap.put(key, schedule);

        }catch(Exception e){
            log.error("定时任务启动失败, jobName={}", job.getJobName(), e);
        }
    }

    public void stopJob(Job job){
        String key = Optional.ofNullable(job)
                .map(o-> o.getId()).map(o-> o.toString())
                .orElseThrow(() -> new RuntimeException("定时任务停止失败, 未配置MyJobApi"));
        ScheduledFuture<?> scheduledFuture = taskMap.get(key);
        if(scheduledFuture != null){
            scheduledFuture.cancel(true);
        }
        keys.remove(key);
        jobs.remove(key);
        taskMap.remove(key);
    }

    public void run(Job job){
        JobHistory jobHistory = new JobHistory();
        jobHistory.setId(IdUtils.snowflakeId());
        jobHistory.setJobId(job.getId());
        jobHistory.setRunTime(new Timestamp(System.currentTimeMillis()));
        jobHistory.setStatus(JobHistory.Status.run);
        jobHistoryMapper.insert(jobHistory);

        try{
            String beanName = job.getBeanName();
            String methodName = job.getMethodName();

            Object springBeanService = SpringUtils.getBean(beanName);
            Method method = Arrays.stream(springBeanService.getClass().getMethods()).filter(o -> o.getName().equals(methodName)).collect(Collectors.toList()).get(0);
            method.invoke(springBeanService, null);

            jobHistory.setStatus(JobHistory.Status.ok);
            jobHistory.setStopTime(new Timestamp(System.currentTimeMillis()));

            jobHistoryMapper.updateById(jobHistory);

        }catch(InvocationTargetException e){
            log.error("job执行异常, jobName={}", job.getJobName(), e);

            Throwable targetException = e.getTargetException();
            String exception = ExceptionUtils.getStackTrace(targetException);

            jobHistory.setException(exception);
            jobHistory.setStatus(JobHistory.Status.ex);
            jobHistory.setStopTime(new Timestamp(System.currentTimeMillis()));

            jobHistoryMapper.updateById(jobHistory);

        }catch(Exception e){
            log.error("job执行异常, jobName={}", job.getJobName(), e);

            String exception = ExceptionUtils.getStackTrace(e);

            jobHistory.setException(exception);
            jobHistory.setStatus(JobHistory.Status.ex);
            jobHistory.setStopTime(new Timestamp(System.currentTimeMillis()));

            jobHistoryMapper.updateById(jobHistory);

        }
    }
}
