package com.vict.bean.organize.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.vict.framework.fastjsonserializer.LongDeserializer;
import com.vict.framework.fastjsonserializer.LongSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class OrganizeVO {
    @ApiModelProperty("id")
    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    private Long id;

    @ApiModelProperty("fatherId")
    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    private Long fatherId;

    @ApiModelProperty("名称")
    private String organizeName;

    @ApiModelProperty("子部门")
    private List<OrganizeVO> children;
}
