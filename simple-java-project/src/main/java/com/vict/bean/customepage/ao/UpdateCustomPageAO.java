package com.vict.bean.customepage.ao;

import com.alibaba.fastjson.annotation.JSONField;
import com.vict.framework.fastjsonserializer.LongDeserializer;
import com.vict.framework.fastjsonserializer.LongSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateCustomPageAO {

    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    private Long id;

    @ApiModelProperty("页面名称")
    private String customName;

    @ApiModelProperty("页面内容")
    private String pageValue;

    public void check(){
        if (customName == null || customName.equals("")) {
            throw new RuntimeException("页面名称不能为空");
        }
        if(id == null){
            throw new RuntimeException("id不能为空");
        }
    }
}
