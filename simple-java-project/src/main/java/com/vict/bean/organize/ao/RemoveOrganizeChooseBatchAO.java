package com.vict.bean.organize.ao;

import com.alibaba.fastjson.annotation.JSONField;
import com.vict.framework.fastjsonserializer.LongDeserializer;
import com.vict.framework.fastjsonserializer.LongListDeserializer;
import com.vict.framework.fastjsonserializer.LongListSerializer;
import com.vict.framework.fastjsonserializer.LongSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class RemoveOrganizeChooseBatchAO {
    @ApiModelProperty("组织id")
    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    private Long organizeId;

    @ApiModelProperty("buser id列表")
    @JSONField(serializeUsing = LongListSerializer.class, deserializeUsing = LongListDeserializer.class)
    private List<Long> buserIds;
}
