package com.vict.bean.applogin.dto;

import com.vict.entity.Customer;
import lombok.Data;

@Data
public class AppToken {
    private String aToken;

    private Customer customer;
}
