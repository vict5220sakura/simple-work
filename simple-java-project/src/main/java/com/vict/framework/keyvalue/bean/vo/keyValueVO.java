package com.vict.framework.keyvalue.bean.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.vict.framework.fastjsonserializer.LongDeserializer;
import com.vict.framework.fastjsonserializer.LongSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class keyValueVO {

    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("键")
    private String key;

    @ApiModelProperty("值1")
    private String value1;

    @ApiModelProperty("值2")
    private String value2;

    @ApiModelProperty("状态")
    private String desc;

    @ApiModelProperty("隐藏标记")
    private Integer hiddenFlag;
}
