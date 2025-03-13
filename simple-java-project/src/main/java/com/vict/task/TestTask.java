package com.vict.task;

import com.vict.framework.task.annotation.TaskConsumerConfiguration;
import com.vict.framework.task.bean.dto.Task;
import com.vict.framework.task.bean.dto.TaskConsumeResult;
import com.vict.framework.task.core.TaskExecute;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@TaskConsumerConfiguration(type = "testTask", sameTimeWorkNum = 2, failRetryTimeSeconds = {5, 5})
public class TestTask implements TaskExecute<String> {
    @Override
    public TaskConsumeResult doConsume(Task task, String s) {
        log.info("task异常测试" + s);
        throw new RuntimeException("111");
        // return task.commitConsumer();
    }
}
