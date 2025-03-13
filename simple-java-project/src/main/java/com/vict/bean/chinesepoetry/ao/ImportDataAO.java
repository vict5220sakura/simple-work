package com.vict.bean.chinesepoetry.ao;

import com.alibaba.fastjson.annotation.JSONField;
import com.vict.framework.fastjsonserializer.LongDeserializer;
import com.vict.framework.fastjsonserializer.LongSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ImportDataAO {

    @ApiModelProperty("类别id")
    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    private Long classifyId;

    private String fileUrl;

    private String importData;

    public void check() {
        if ((fileUrl == null || fileUrl.trim().length() == 0) && (importData == null || importData.trim().length() == 0)) {
            throw new RuntimeException("请选择文件或输入数据");
        }
        if (classifyId == null) {
            throw new RuntimeException("类别不能为空");
        }
    }
}
