package com.vict.framework.keyvalue.bean.ao;

import com.vict.framework.bean.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class KeyValueListAO extends PageRequest {
    @ApiModelProperty("键")
    private String key;

    @ApiModelProperty("状态")
    private String desc;
}
