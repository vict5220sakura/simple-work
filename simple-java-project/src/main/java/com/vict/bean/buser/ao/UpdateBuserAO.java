package com.vict.bean.buser.ao;

import com.alibaba.fastjson.annotation.JSONField;
import com.vict.framework.fastjsonserializer.LongDeserializer;
import com.vict.framework.fastjsonserializer.LongSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateBuserAO {

    @ApiModelProperty("id")
    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("员工编号")
    private String buserCode;

    @ApiModelProperty("角色id")
    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    private Long roleId;

    public void check() {
        if(id == null){
            throw new RuntimeException("员工编号不可为空");
        }
        if(buserCode == null || buserCode.trim().equals("")){
            throw new RuntimeException("员工编号不可为空");
        }
    }
}
