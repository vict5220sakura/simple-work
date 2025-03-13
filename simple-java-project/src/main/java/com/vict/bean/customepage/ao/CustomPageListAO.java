package com.vict.bean.customepage.ao;

import com.vict.framework.bean.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CustomPageListAO extends PageRequest {
    @ApiModelProperty("页面名称")
    private String customName;
}
