package com.vict.bean.buser.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.vict.entity.Buser;
import com.vict.entity.Role;
import com.vict.framework.fastjsonserializer.LongDeserializer;
import com.vict.framework.fastjsonserializer.LongSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Optional;

@Data
public class BuserVO {

    @JSONField(serialize = false)
    private Buser buser;

    @JSONField(serialize = false)
    private Role role;

    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    public Long getId(){
        return buser.getId();
    }

    @ApiModelProperty("用户名")
    public String getUsername(){
        return buser.getUsername();
    }

    @ApiModelProperty("员工编号")
    public String getBuserCode(){
        return buser.getBuserCode();
    }

    @ApiModelProperty("角色id")
    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    public Long getRoleId(){
        return Optional.ofNullable(role).map(o-> o.getId()).orElse(null);
    }

    @ApiModelProperty("角色名")
    public String getRolename(){
        return Optional.ofNullable(role).map(o-> o.getRolename()).orElse(null);
    }
}
