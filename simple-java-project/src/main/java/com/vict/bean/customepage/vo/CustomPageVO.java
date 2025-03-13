package com.vict.bean.customepage.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.vict.entity.CustomPage;
import com.vict.framework.fastjsonserializer.LongDeserializer;
import com.vict.framework.fastjsonserializer.LongSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Optional;

@Data
public class CustomPageVO {
    private CustomPage customPage;

    @ApiModelProperty("id")
    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    public Long getId(){
        return Optional.ofNullable(customPage).map(o -> o.getId()).orElse(null);
    }

    @ApiModelProperty("名称")
    public String getCustomName(){
        return Optional.ofNullable(customPage).map(o -> o.getCustomName()).orElse(null);
    }

    @ApiModelProperty("页面内容")
    public String getPageValue(){
        return Optional.ofNullable(customPage).map(o -> o.getPageValue()).orElse(null);
    }
}
