package com.vict.bean.chinesepoetry.ao;

import com.alibaba.fastjson.annotation.JSONField;
import com.vict.framework.fastjsonserializer.LongDeserializer;
import com.vict.framework.fastjsonserializer.LongSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateChinesePoetryClassifyAO {

    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    private Long id;

    @ApiModelProperty("分类名称")
    private String classifyName;

    public void check() {
        if (id == null) {
            throw new RuntimeException("id不能为空");
        }
        if (classifyName == null) {
            throw new RuntimeException("分类名称不能为空");
        }
    }
}
