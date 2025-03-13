package com.vict.framework.bean;

import lombok.Data;

/**
 * 上下文对象
 */
@Data
public class UserContext {

    /** 本次请求id */
    private String requestId;

    /** 本次请求用户token */
    private String token;

    /** 本次请求APP用户token */
    private String aToken;

    private Long buserId;

    private Long appCustomerId;
}
