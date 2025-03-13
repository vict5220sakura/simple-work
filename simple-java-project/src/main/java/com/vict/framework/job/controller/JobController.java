package com.vict.framework.job.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.vict.bean.customer.ao.InsertCustomerAO;
import com.vict.framework.bean.MyPageInfo;
import com.vict.framework.bean.R;
import com.vict.framework.job.bean.ao.*;
import com.vict.framework.job.bean.vo.JobHistoryVO;
import com.vict.framework.job.bean.vo.JobVO;
import com.vict.framework.job.core.JobService;
import com.vict.framework.job.entity.Job;
import com.vict.framework.job.entity.JobHistory;
import com.vict.framework.job.mapper.JobHistoryMapper;
import com.vict.framework.job.mapper.JobMapper;
import com.vict.framework.keyvalue.entity.KeyValue;
import com.vict.framework.utils.lock.LockApi;
import com.vict.framework.web.ApiPrePath;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@ApiPrePath
@Api
@Slf4j
@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private JobService jobService;

    @Autowired
    private JobHistoryMapper jobHistoryMapper;

    public static ExecutorService jobActionPool = new ThreadPoolExecutor(10, 20, 99999, TimeUnit.DAYS, new ArrayBlockingQueue<>(10000));

    @ApiOperation(value = "新增定时任务", notes = "新增定时任务")
    @PostMapping("/insertJob")
    public R insertJob(@RequestBody InsertJobAO insertJobAO) {
        insertJobAO.check();

        Job job = new Job();

        BeanUtils.copyProperties(insertJobAO, job);
        job.setStatus(Job.Status.disabled);
        jobMapper.insert(job);

        return R.ok();
    }

    @ApiOperation(value = "定时任务列表", notes = "定时任务列表")
    @PostMapping("/jobList")
    public R<MyPageInfo<JobVO>> jobList(@RequestBody JobListAO jobListAO) {

        String jobName = Optional.ofNullable(jobListAO).map(o -> o.getJobName()).map(o -> o.trim()).filter(o -> !o.equals("")).orElse(null);
        String beanName = Optional.ofNullable(jobListAO).map(o -> o.getBeanName()).map(o -> o.trim()).filter(o -> !o.equals("")).orElse(null);
        String methodName = Optional.ofNullable(jobListAO).map(o -> o.getMethodName()).map(o -> o.trim()).filter(o -> !o.equals("")).orElse(null);
        Job.Status status = Optional.ofNullable(jobListAO).map(o -> o.getStatus()).orElse(null);

        Page<Job> page = PageHelper.startPage(jobListAO.getPageNum(), jobListAO.getPageSize());

        jobMapper.list(jobName, beanName, methodName, status);

        // 参数封装
        MyPageInfo<JobVO> jobVOMyPageInfo = MyPageInfo.copyPageNoList(page, JobVO.class);
        for(Job job : Optional.ofNullable(page).map(o-> o.getResult()).orElse(new ArrayList<>())){
            JobVO jobVO = new JobVO();
            jobVO.setJob(job);
            jobVOMyPageInfo.getList().add(jobVO);
        }

        return R.ok(jobVOMyPageInfo);
    }

    @ApiOperation(value = "定时任务删除", notes = "定时任务删除")
    @PostMapping("/deleteJobById")
    public R deleteJobById(@RequestBody DeleteJobByIdAO deleteJobByIdAO) {
        jobMapper.deleteById(deleteJobByIdAO.getId());
        return R.ok();
    }

    @ApiOperation(value = "定时任务详情", notes = "定时任务详情")
    @PostMapping("/jobInfo")
    public R<JobVO> jobInfo(@RequestBody JobInfoAO jobInfoAO) {

        Long id = jobInfoAO.getId();

        Job job = jobMapper.selectById(id);

        JobVO jobVO = new JobVO();
        jobVO.setJob(job);

        return R.ok(jobVO);
    }

    @ApiOperation(value = "更新定时任务", notes = "更新定时任务")
    @PostMapping("/updateJobById")
    public R updateJobById(@RequestBody UpdateJobByIdAO updateJobByIdAO) {
        updateJobByIdAO.check();
        Job job = jobMapper.selectById(updateJobByIdAO.getId());
        BeanUtils.copyProperties(updateJobByIdAO, job);
        jobMapper.updateById(job);

        return R.ok();
    }

    @ApiOperation(value = "启用", notes = "启用")
    @PostMapping("/startup")
    public R startup(@RequestBody StartupAO startupAO) {
        Job job = jobMapper.selectById(startupAO.getId());
        job.setStatus(Job.Status.enable);
        jobMapper.updateById(job);

        jobService.startRun(job);

        return R.ok();
    }

    @ApiOperation(value = "禁用", notes = "禁用")
    @PostMapping("/stop")
    public R stop(@RequestBody StopAO stopAO) {
        Job job = jobMapper.selectById(stopAO.getId());
        job.setStatus(Job.Status.disabled);
        jobMapper.updateById(job);

        jobService.stopJob(job);

        return R.ok();
    }

    @ApiOperation(value = "定时任务运行历史列表", notes = "定时任务运行历史列表")
    @PostMapping("/jobHistoryList")
    public R<MyPageInfo<JobHistoryVO>> jobHistoryList(@RequestBody JobHistoryListAO jobHistoryListAO) {

        Timestamp startTime = Optional.ofNullable(jobHistoryListAO).map(o -> o.getStartTime()).orElse(null);
        Timestamp endTime = Optional.ofNullable(jobHistoryListAO).map(o -> o.getEndTime()).orElse(null);
        JobHistory.Status status = Optional.ofNullable(jobHistoryListAO).map(o -> o.getStatus()).orElse(null);

        Page<JobHistory> page = PageHelper.startPage(jobHistoryListAO.getPageNum(), jobHistoryListAO.getPageSize());

        jobHistoryMapper.list(startTime, endTime, status);

        // 参数封装
        MyPageInfo<JobHistoryVO> myPageInfo = MyPageInfo.copyPageNoList(page, JobHistoryVO.class);
        for(JobHistory jobHistory : Optional.ofNullable(page).map(o-> o.getResult()).orElse(new ArrayList<>())){
            JobHistoryVO jobHistoryVO = new JobHistoryVO();
            jobHistoryVO.setJobHistory(jobHistory);
            myPageInfo.getList().add(jobHistoryVO);
        }

        return R.ok(myPageInfo);
    }

    @ApiOperation(value = "启用", notes = "启用")
    @PostMapping("/reStart")
    public R reStart(@RequestBody ReStartAO reStartAO) {
        Job job = jobMapper.selectById(reStartAO.getId());
        job.setStatus(Job.Status.disabled);
        jobMapper.updateById(job);

        jobService.stopJob(job);


        job.setStatus(Job.Status.enable);
        jobMapper.updateById(job);

        jobService.startRun(job);

        return R.ok();
    }



    @ApiOperation(value = "触发", notes = "触发")
    @PostMapping("/actionJob")
    public R actionJob(@RequestBody ActionJobAO actionJobAO) {
        Job job = jobMapper.selectById(actionJobAO.getId());
        jobActionPool.submit(()->{
                jobService.run(job);
        });

        return R.ok();
    }
}
