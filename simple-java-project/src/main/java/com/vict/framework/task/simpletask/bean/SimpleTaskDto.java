package com.vict.framework.task.simpletask.bean;

import lombok.Data;

@Data
public class SimpleTaskDto {
    private String className;

    private String methodName;

    /** @Type ParamDtoList */
    private SimpleTaskParamDtoList args;
}
