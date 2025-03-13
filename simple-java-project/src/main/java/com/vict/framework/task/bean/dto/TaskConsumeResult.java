package com.vict.framework.task.bean.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TaskConsumeResult implements Serializable {
    private static final long serialVersionUID = 7492540435607246825L;

    private int result;
    public static final int result_commitConsumer = 0;
    public static final int result_reconsumeLaterConsumer = 1;
    public static final int result_failConsumer = 2;
}
