package com.vict.bean.chinesepoetry.ao;

import com.alibaba.fastjson.annotation.JSONField;
import com.vict.framework.fastjsonserializer.LongDeserializer;
import com.vict.framework.fastjsonserializer.LongListDeserializer;
import com.vict.framework.fastjsonserializer.LongListSerializer;
import com.vict.framework.fastjsonserializer.LongSerializer;
import lombok.Data;

import java.util.List;

@Data
public class ChangeChinesePoetryClassifyAO {

    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    private Long chinesePoetryClassifyId;

    @JSONField(serializeUsing = LongListSerializer.class, deserializeUsing = LongListDeserializer.class)
    private List<Long> chinesePoetryIds;

    public void check() {
        if (chinesePoetryClassifyId == null) {
            throw new RuntimeException("请选择类目");
        }
        if (chinesePoetryIds == null || chinesePoetryIds.size() == 0) {
            throw new RuntimeException("请选择需要修改类目的古诗");
        }
    }
}
