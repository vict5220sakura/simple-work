package com.vict.bean.buserlogin.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.vict.framework.fastjsonserializer.EnumDeserializer;
import com.vict.framework.fastjsonserializer.EnumSerializer;
import com.vict.framework.myannotation.MyDescription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
public class IsLoginVO {

    @MyDescription("是否登录")
    @Getter
    @AllArgsConstructor
    public enum IsLogin implements IEnum<String> {
        loginIn("loginIn", "登录"),
        loginOut("loginOut", "未登录"),
        ;
        private String value;
        private String name;
    }

    @JSONField(serializeUsing = EnumSerializer.class, deserializeUsing = EnumDeserializer.class)
    private IsLogin isLogin;
}
