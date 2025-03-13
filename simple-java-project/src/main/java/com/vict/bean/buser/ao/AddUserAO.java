package com.vict.bean.buser.ao;

import com.alibaba.fastjson.annotation.JSONField;
import com.vict.framework.fastjsonserializer.LongDeserializer;
import com.vict.framework.fastjsonserializer.LongSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AddUserAO {
    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("员工编号")
    private String bUserCode;

    @ApiModelProperty("角色id")
    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    private Long roleId;

    public void check(){
        if(username == null || username.trim().length() == 0){
            throw new RuntimeException("用户名不能为空");
        }
        if(password == null || password.trim().length() == 0){
            throw new RuntimeException("密码不能为空");
        }
        if(bUserCode == null || bUserCode.trim().length() == 0){
            throw new RuntimeException("员工编号不能为空");
        }
    }
}
