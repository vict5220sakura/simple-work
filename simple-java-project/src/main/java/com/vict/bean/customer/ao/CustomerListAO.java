package com.vict.bean.customer.ao;

import com.vict.framework.bean.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CustomerListAO extends PageRequest {

    @ApiModelProperty("手机号")
    private String customerPhone;

    @ApiModelProperty("姓名")
    private String customerName;
}
