package com.vict.bean.chinesepoetry.ao;

import com.alibaba.fastjson.annotation.JSONField;
import com.vict.framework.fastjsonserializer.LongListDeserializer;
import com.vict.framework.fastjsonserializer.LongListSerializer;
import lombok.Data;

import java.util.List;

@Data
public class DeleteChinesePoetryManyAO {

    @JSONField(serializeUsing = LongListSerializer.class, deserializeUsing = LongListDeserializer.class)
    private List<Long> chinesePoetryIds;

    public void check(){
        if (chinesePoetryIds == null || chinesePoetryIds.size() == 0) {
            throw new RuntimeException("请选择需要删除的古诗");
        }
    }

}
