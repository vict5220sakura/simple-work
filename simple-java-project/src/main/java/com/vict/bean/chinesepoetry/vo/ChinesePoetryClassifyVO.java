package com.vict.bean.chinesepoetry.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.vict.entity.chinesepoetry.ChinesePoetryClassify;
import com.vict.framework.fastjsonserializer.LongDeserializer;
import com.vict.framework.fastjsonserializer.LongSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Optional;

@Data
public class ChinesePoetryClassifyVO {

    @JSONField(serialize = false, deserialize = false)
    private ChinesePoetryClassify chinesePoetryClassify;

    @ApiModelProperty("id")
    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    public Long getId(){
        return Optional.ofNullable(this.chinesePoetryClassify).map(o-> o.getId()).orElse(null);
    }

    @ApiModelProperty("分类名称")
    public String getClassifyName(){
        return Optional.ofNullable(this.chinesePoetryClassify).map(o-> o.getClassifyName()).orElse(null);
    }
}
