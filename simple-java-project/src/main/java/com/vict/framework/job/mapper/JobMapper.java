package com.vict.framework.job.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vict.framework.job.entity.Job;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface JobMapper extends BaseMapper<Job> {

    List<Job> list(@Param("jobName")String jobName,
                   @Param("beanName")String beanName,
                   @Param("methodName")String methodName,
                   @Param("status")Job.Status status);
}
