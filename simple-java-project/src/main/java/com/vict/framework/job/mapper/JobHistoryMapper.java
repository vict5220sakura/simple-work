package com.vict.framework.job.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vict.framework.job.entity.JobHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface JobHistoryMapper extends BaseMapper<JobHistory> {
    List<JobHistory> list(@Param("startTime")Timestamp startTime,
                          @Param("endTime")Timestamp endTime,
                          @Param("status")JobHistory.Status status);
}
