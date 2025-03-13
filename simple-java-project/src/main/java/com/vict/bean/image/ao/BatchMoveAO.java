package com.vict.bean.image.ao;

import com.alibaba.fastjson.annotation.JSONField;
import com.vict.framework.fastjsonserializer.LongListDeserializer;
import com.vict.framework.fastjsonserializer.LongListSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class BatchMoveAO {

    @ApiModelProperty("id列表")
    @JSONField(serializeUsing = LongListSerializer.class, deserializeUsing = LongListDeserializer.class)
    private List<Long> ids;

    public void check(){
        if (ids == null || ids.size() == 0) {
            throw new RuntimeException("要排序的内容不可为空");
        }
    }
}
