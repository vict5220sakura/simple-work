package com.vict.bean.role.ao;

import com.alibaba.fastjson.annotation.JSONField;
import com.vict.framework.fastjsonserializer.LongDeserializer;
import com.vict.framework.fastjsonserializer.LongSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeleteRoleAO {
    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    @ApiModelProperty("id")
    private Long id;

    public void check() {
        if (id == null) {
            throw new RuntimeException("id不能为空");
        }
    }
}
