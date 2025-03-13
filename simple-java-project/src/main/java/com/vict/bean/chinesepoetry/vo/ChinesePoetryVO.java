package com.vict.bean.chinesepoetry.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.vict.entity.chinesepoetry.ChinesePoetry;
import com.vict.framework.fastjsonserializer.LongDeserializer;
import com.vict.framework.fastjsonserializer.LongSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Optional;

@Data
public class ChinesePoetryVO {

    @JSONField(serialize = false, deserialize = false)
    private ChinesePoetry chinesePoetry;

    @ApiModelProperty("id")
    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    public Long getId(){
        return Optional.ofNullable(chinesePoetry).map(o-> o.getId()).orElse(null);
    }

    @ApiModelProperty("标题")
    public String getTitle(){
        return Optional.ofNullable(chinesePoetry).map(o-> o.getTitle()).map(o-> o.trim()).orElse(null);
    }

    @ApiModelProperty("作者")
    public String getAuthor(){
        return Optional.ofNullable(chinesePoetry).map(o-> o.getAuthor()).map(o-> o.trim()).orElse(null);
    }

    @ApiModelProperty("内容")
    public String getParagraphs(){
        return Optional.ofNullable(chinesePoetry).map(o-> o.getParagraphs()).map(o-> o.trim()).orElse(null);
    }

    @ApiModelProperty("类别id")
    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    public Long getClassifyId(){
        return Optional.ofNullable(chinesePoetry).map(o-> o.getClassifyId()).orElse(null);
    }
}
