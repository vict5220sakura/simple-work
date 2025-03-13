package com.vict.bean.ws.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.vict.framework.fastjsonserializer.EnumDeserializer;
import com.vict.framework.fastjsonserializer.EnumSerializer;
import com.vict.framework.fastjsonserializer.LongDeserializer;
import com.vict.framework.fastjsonserializer.LongSerializer;
import com.vict.framework.myannotation.MyDescription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
public class MessageDTO {

    @MyDescription("是否选中")
    @Getter
    @AllArgsConstructor
    public enum MessageType implements IEnum<String> {
        ping("ping", "心跳请求"),
        pong("pong", "心跳响应"),
        testMessage("testMessage", "测试消息"),
        testMessage2("testMessage2", "测试消息2"),
        appTestMessage("appTestMessage", "appTestMessage")
        ;
        private String value;
        private String name;
    }

    @JSONField(serializeUsing = EnumSerializer.class, deserializeUsing = EnumDeserializer.class)
    private MessageType type;

    private String data;

    // @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    // private Long id;
}
