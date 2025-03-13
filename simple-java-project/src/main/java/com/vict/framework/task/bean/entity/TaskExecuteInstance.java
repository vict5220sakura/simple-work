package com.vict.framework.task.bean.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@TableName("task_execute_instance")
public class TaskExecuteInstance implements Serializable {
    private static final long serialVersionUID = 4782965139950730668L;

    @TableId("taskExecuteInstanceID")
    private String taskExecuteInstanceID;
    @TableField("serverName")
    private String serverName;
    @TableField("serverId")
    private String serverId;
    @TableField("type")
    private String type;
    @TableField("status")
    private Integer status; // '任务状态 0待执行 1执行中 2已完成 3已失败',
    public static final int status_await = 0;
    public static final int status_run = 1;
    public static final int status_finish = 2;
    public static final int status_fail = 3;
    @TableField("executeCount")
    private Integer executeCount;
    @TableField("actionTime")
    private Timestamp actionTime;
    @TableField("executeJSON")
    private String executeJSON;
    @TableField("userContext")
    private String userContext;

    @TableField("failRetryTimeSeconds")
    private String failRetryTimeSeconds;

    // 非数据库字段
    @TableField(exist = false)
    private String actionTimeList;
    @TableField(exist = false)
    private String taskExecuteInstanceIDList;
    @TableField(exist = false)
    private String createdList;
    @TableField(exist = false)
    private String statusList;
    @TableField(exist = false)
    private String executeCountList;
    @TableField(exist = false)
    private String executeJSONList;
}
