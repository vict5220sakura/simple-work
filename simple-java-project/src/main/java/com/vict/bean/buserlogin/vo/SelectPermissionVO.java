package com.vict.bean.buserlogin.vo;

import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SelectPermissionVO {
    @ApiModelProperty("权限列表")
    private JSONArray permissionList;
}
