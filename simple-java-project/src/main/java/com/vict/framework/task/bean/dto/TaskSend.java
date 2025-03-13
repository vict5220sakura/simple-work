package com.vict.framework.task.bean.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class TaskSend<T> implements Serializable {
    private static final long serialVersionUID = -2747703060148243892L;
    private String type;
    private T t;
    private Timestamp actionTime;
}
