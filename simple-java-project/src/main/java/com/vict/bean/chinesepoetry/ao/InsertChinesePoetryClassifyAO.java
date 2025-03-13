package com.vict.bean.chinesepoetry.ao;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class InsertChinesePoetryClassifyAO {
    @ApiModelProperty("分类名称")
    private String classifyName;

    public void check(){
        if (classifyName == null) {
            throw new RuntimeException("分类名称不能为空");
        }
    }
}
