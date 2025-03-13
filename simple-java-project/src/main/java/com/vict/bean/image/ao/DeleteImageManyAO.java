package com.vict.bean.image.ao;

import com.alibaba.fastjson.annotation.JSONField;
import com.vict.framework.fastjsonserializer.LongListDeserializer;
import com.vict.framework.fastjsonserializer.LongListSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DeleteImageManyAO {

    @JSONField(serializeUsing = LongListSerializer.class,deserializeUsing = LongListDeserializer.class)
    @ApiModelProperty(value = "图片id集合")
    private List<Long> ids;

    public void check(){
        if(ids == null || ids.size() == 0){
            throw new RuntimeException("ids不能为空");
        }
    }
}
