package com.vict.bean.role.ao;

import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AddRoleAO {

    @ApiModelProperty("角色名称")
    private String rolename;

    @ApiModelProperty("权限列表")
    private JSONArray permissionList;

    public void check() {
        if (rolename == null || rolename.length() == 0) {
            throw new RuntimeException("角色名称不能为空");
        }
    }
}
