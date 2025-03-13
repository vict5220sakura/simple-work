package com.vict.bean.image.ao;

import com.alibaba.fastjson.annotation.JSONField;
import com.vict.framework.fastjsonserializer.LongDeserializer;
import com.vict.framework.fastjsonserializer.LongSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateImageAttnameAO {
    @ApiModelProperty("id")
    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    private Long id;

    @ApiModelProperty("别名")
    private String attname;

    public void check() {
        if (id == null) {
            throw new RuntimeException("id不能为空");
        }
        if (attname == null || "".equals(attname.trim())) {
            throw new RuntimeException("图片名称不能为空");
        }
    }
}
