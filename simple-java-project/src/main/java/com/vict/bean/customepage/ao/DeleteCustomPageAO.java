package com.vict.bean.customepage.ao;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.vict.framework.fastjsonserializer.LongDeserializer;
import com.vict.framework.fastjsonserializer.LongSerializer;
import lombok.Data;

@Data
public class DeleteCustomPageAO {
    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    private Long id;

    public void check(){
        if(id == null){
            throw new RuntimeException("id不能为空");
        }
    }
}
