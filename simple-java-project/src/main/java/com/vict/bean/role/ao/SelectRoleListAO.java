package com.vict.bean.role.ao;

import com.vict.framework.bean.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SelectRoleListAO extends PageRequest {
    @ApiModelProperty("角色名称")
    private String rolename;
}
