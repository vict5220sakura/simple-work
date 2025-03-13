package com.vict.framework.task.core;

import com.vict.framework.task.bean.dto.TaskSend;

import java.util.List;

public interface TaskService {
    void commitConsumer(String taskExecuteInstanceID);
    void commitConsumer(String taskExecuteInstanceID, List<TaskSend> sendTaskList);
    void commitConsumer(String taskExecuteInstanceID, TaskSend sendTask);
}
