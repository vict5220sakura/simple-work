package com.vict.bean.customer.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.vict.entity.Customer;
import com.vict.framework.fastjsonserializer.LongDeserializer;
import com.vict.framework.fastjsonserializer.LongSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Optional;

@Data
public class CustomerItemVO {

    @JSONField(serialize = false, deserialize = false)
    private Customer customer;


    @ApiModelProperty("id")
    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    public Long getId(){
        return Optional.ofNullable(this.customer).map(o-> o.getId()).orElse(null);
    }

    @ApiModelProperty("手机号")
    public String getCustomerPhone(){
        return Optional.ofNullable(this.customer).map(o-> o.getCustomerPhone()).orElse(null);
    }

    @ApiModelProperty("姓名")
    public String getCustomerName(){
        return Optional.ofNullable(this.customer).map(o-> o.getCustomerName()).orElse(null);
    }
}
