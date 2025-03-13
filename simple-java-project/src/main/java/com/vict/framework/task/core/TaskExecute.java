package com.vict.framework.task.core;

import com.vict.framework.task.bean.dto.Task;
import com.vict.framework.task.bean.dto.TaskConsumeResult;

public interface TaskExecute<T> {
    TaskConsumeResult doConsume(Task task, T t);
}
