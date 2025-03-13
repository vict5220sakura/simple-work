package com.vict.bean.image.ao;

import com.alibaba.fastjson.annotation.JSONField;
import com.vict.framework.fastjsonserializer.LongDeserializer;
import com.vict.framework.fastjsonserializer.LongSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ChangeImageAO {

    @ApiModelProperty("id1")
    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    private Long id1;

    @ApiModelProperty("id2")
    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    private Long id2;

    public void check() {
        if (id1 == null || id2 == null) {
            throw new RuntimeException("id1和id2不能为空");
        }
    }
}
