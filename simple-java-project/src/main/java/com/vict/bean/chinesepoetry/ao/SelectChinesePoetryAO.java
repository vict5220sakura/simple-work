package com.vict.bean.chinesepoetry.ao;

import com.alibaba.fastjson.annotation.JSONField;
import com.vict.framework.bean.PageRequest;
import com.vict.framework.fastjsonserializer.LongDeserializer;
import com.vict.framework.fastjsonserializer.LongSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SelectChinesePoetryAO extends PageRequest {
    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("作者")
    private String author;

    @ApiModelProperty("类别id")
    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    private Long classifyId;
}
