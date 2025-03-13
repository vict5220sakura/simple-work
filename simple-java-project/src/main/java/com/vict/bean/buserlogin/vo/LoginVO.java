package com.vict.bean.buserlogin.vo;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

@Data
public class LoginVO {
    private String token;
    private JSONArray permissionList;
    private String buserCode;
}
