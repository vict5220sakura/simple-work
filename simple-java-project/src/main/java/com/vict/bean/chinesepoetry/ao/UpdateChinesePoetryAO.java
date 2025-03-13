package com.vict.bean.chinesepoetry.ao;

import com.alibaba.fastjson.annotation.JSONField;
import com.vict.framework.fastjsonserializer.LongDeserializer;
import com.vict.framework.fastjsonserializer.LongSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateChinesePoetryAO {
    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    private Long id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("作者")
    private String author;

    @ApiModelProperty("内容")
    private String paragraphs;

    public void check(){
        if (id == null) {
            throw new RuntimeException("id不能为空");
        }
        if (title == null || title.trim().equals("")) {
            throw new RuntimeException("标题不能为空");
        }
        if (author == null || author.trim().equals("")) {
            throw new RuntimeException("作者不能为空");
        }
        if (paragraphs == null || paragraphs.trim().equals("")) {
            throw new RuntimeException("内容不能为空");
        }
    }
}
