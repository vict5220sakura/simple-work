package com.vict.framework.task.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vict.framework.task.bean.entity.TaskExecuteInstance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface TaskExecuteInstanceDao extends BaseMapper<TaskExecuteInstance> {

    /**
     * 根据社区分组查询进行中的任务
     */
    List<TaskExecuteInstance> selectGroup(@Param("serverName")String serverName,
                                          @Param("serverId")String serverId,
                                          @Param("type")String type,
                                          @Param("limit")Integer limit,
                                          @Param("now")Timestamp now
    );
    int updateStatusById(@Param("id")String id,
                         @Param("status")Integer status
    );

    int updateExecuteCount(@Param("id")String id,
                           @Param("executeCount")Integer executeCount
    );
}
