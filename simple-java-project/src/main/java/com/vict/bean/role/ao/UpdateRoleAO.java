package com.vict.bean.role.ao;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;
import com.vict.framework.fastjsonserializer.LongDeserializer;
import com.vict.framework.fastjsonserializer.LongSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateRoleAO {

    @ApiModelProperty("id")
    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    private Long id;

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
