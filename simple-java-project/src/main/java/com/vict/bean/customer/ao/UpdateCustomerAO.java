package com.vict.bean.customer.ao;

import com.alibaba.fastjson.annotation.JSONField;
import com.vict.framework.fastjsonserializer.LongDeserializer;
import com.vict.framework.fastjsonserializer.LongSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateCustomerAO {

    @ApiModelProperty("id")
    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    private Long id;

    @ApiModelProperty("手机号")
    private String customerPhone;

    @ApiModelProperty("姓名")
    private String customerName;

    public void check(){
        if (id == null) {
            throw new RuntimeException("id不能为空");
        }
        if (customerPhone == null || customerPhone.equals("")) {
            throw new RuntimeException("手机号不能为空");
        }
        if (customerName == null || customerName.equals("")) {
            throw new RuntimeException("姓名不能为空");
        }
    }
}
