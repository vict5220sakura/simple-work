package com.vict.framework.task.util;

import com.vict.framework.task.core.impl.TaskExecuteProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class TaskExecuteUtil {
    public static TaskExecuteProcessor taskExecuteProcessor;

    @Autowired
    public void setTaskExecuteProcessor(TaskExecuteProcessor taskExecuteProcessor){
        TaskExecuteUtil.taskExecuteProcessor = taskExecuteProcessor;
    }

    public static void startTaskExecuteProcessor(String type){
        taskExecuteProcessor.startTaskExecuteProcessor(type);
    }

    public static <T> void sendTask(String type, T t){
        taskExecuteProcessor.sendTask(type, t, new Timestamp(System.currentTimeMillis()), null);
    }

    public static <T> void sendTaskRetryTime(String type, T t, int[] failRetryTimeSeconds){
        taskExecuteProcessor.sendTask(type, t, new Timestamp(System.currentTimeMillis()), failRetryTimeSeconds);
    }

    public static <T> void sendTask(String type, T t, Timestamp actionTime){
        taskExecuteProcessor.sendTask(type, t, actionTime, null);
    }
}
