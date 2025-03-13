package com.vict.bean.customer.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class InsertCustomerAO {
    @ApiModelProperty("手机号")
    private String customerPhone;

    @ApiModelProperty("姓名")
    private String customerName;

    public void check(){
        if (customerPhone == null || customerPhone.equals("")) {
            throw new RuntimeException("手机号不能为空");
        }
        if (customerName == null || customerName.equals("")) {
            throw new RuntimeException("姓名不能为空");
        }
    }
}
