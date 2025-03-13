package com.vict.bean.buserlogin.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginAO {

    @ApiModelProperty("员工工号")
    private String buserCode;

    @ApiModelProperty("密码")
    private String password;

    public void check(){
        if(buserCode == null || buserCode.trim().length() == 0){
            throw new RuntimeException("员工工号不能为空");
        }
        if(password == null || password.trim().length() == 0){
            throw new RuntimeException("密码不能为空");
        }
    }
}
