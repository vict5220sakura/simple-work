package com.vict.framework.keyvalue.bean.ao;

import com.alibaba.fastjson.annotation.JSONField;
import com.vict.framework.fastjsonserializer.LongDeserializer;
import com.vict.framework.fastjsonserializer.LongSerializer;
import lombok.Data;

@Data
public class DeleteKeyValueByIdAO {

    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    private Long id;
}
