package com.vict.bean.buser.ao;

import com.alibaba.fastjson.annotation.JSONField;
import com.vict.framework.bean.PageRequest;
import com.vict.framework.fastjsonserializer.LongDeserializer;
import com.vict.framework.fastjsonserializer.LongSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SelectBuserListAO extends PageRequest {
    private String username;
    private String buserCode;

    @ApiModelProperty("角色id")
    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    private Long roleId;

    @ApiModelProperty("组织id")
    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    private Long organizeId;
}
