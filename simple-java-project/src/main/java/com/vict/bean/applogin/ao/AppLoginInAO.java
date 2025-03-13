package com.vict.bean.applogin.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AppLoginInAO {
    @ApiModelProperty("登录手机号")
    private String phone;

    @ApiModelProperty("密码")
    private String password;

    public void check(){
        if(phone == null || phone.trim().length() == 0){
            throw new RuntimeException("手机号不能为空");
        }
        if(password == null || password.trim().length() == 0){
            throw new RuntimeException("密码不能为空");
        }
    }
}
