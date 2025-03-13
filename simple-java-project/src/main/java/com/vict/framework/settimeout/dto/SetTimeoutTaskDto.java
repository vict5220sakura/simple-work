package com.vict.framework.settimeout.dto;

import com.vict.framework.bean.UserContext;
import lombok.Data;

@Data
public class SetTimeoutTaskDto {
    private String className;

    private String methodName;

    /** @Type ParamDtoList */
    private ParamDtoList args;

    private UserContext userContext;
}
