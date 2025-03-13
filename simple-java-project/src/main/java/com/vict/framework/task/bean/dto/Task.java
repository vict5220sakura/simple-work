package com.vict.framework.task.bean.dto;

import com.vict.framework.task.core.TaskService;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * 任务
 */
@Data
public class Task implements Serializable {
    private static final long serialVersionUID = -7046166422533590378L;
    /**
     * 执行次数
     */
    private Integer executeCount;
    /**
     * 任务id
     */
    private String taskExecuteInstanceID;

    /**
     * 任务执行器
     */
    private TaskService taskService;

    private Timestamp actionTime;

    public TaskConsumeResult commitConsumer(){
        taskService.commitConsumer(this.taskExecuteInstanceID);
        TaskConsumeResult taskConsumeResult = new TaskConsumeResult();
        taskConsumeResult.setResult(TaskConsumeResult.result_commitConsumer);
        return taskConsumeResult;
    }

    public TaskConsumeResult commitConsumer(TaskSend sendTask){
        taskService.commitConsumer(this.taskExecuteInstanceID, sendTask);
        TaskConsumeResult taskConsumeResult = new TaskConsumeResult();
        taskConsumeResult.setResult(TaskConsumeResult.result_commitConsumer);
        return taskConsumeResult;
    }

    public TaskConsumeResult commitConsumer(List<TaskSend> sendTaskList){
        taskService.commitConsumer(this.taskExecuteInstanceID, sendTaskList);
        TaskConsumeResult taskConsumeResult = new TaskConsumeResult();
        taskConsumeResult.setResult(TaskConsumeResult.result_commitConsumer);
        return taskConsumeResult;
    }

    public TaskConsumeResult reconsumeLaterConsumer(){
        TaskConsumeResult taskConsumeResult = new TaskConsumeResult();
        taskConsumeResult.setResult(TaskConsumeResult.result_reconsumeLaterConsumer);
        return taskConsumeResult;
    }

    public TaskConsumeResult failConsumer(){
        TaskConsumeResult taskConsumeResult = new TaskConsumeResult();
        taskConsumeResult.setResult(TaskConsumeResult.result_failConsumer);
        return taskConsumeResult;
    }
}
