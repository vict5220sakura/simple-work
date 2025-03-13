package com.vict.bean.buserlogin.dto;

import lombok.Data;

@Data
public class BUserToken {
    private String token;

    private Long bUserId;

    private String username;

    private String password;

    private String bUserCode;
}
