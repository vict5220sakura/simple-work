package com.vict.entity.chinesepoetry;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.vict.framework.fastjsonserializer.IntegerDeserializer;
import com.vict.framework.fastjsonserializer.IntegerSerializer;
import com.vict.framework.fastjsonserializer.LongDeserializer;
import com.vict.framework.fastjsonserializer.LongSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
@TableName("chinese_poetry_classify")
public class ChinesePoetryClassify {
    @TableId
    @ApiModelProperty("id")
    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    private Long id;

    @TableField("classify_name")
    @ApiModelProperty("分类名称")
    private String classifyName;

    @ApiModelProperty("排序")
    @TableField("order_num")
    @JSONField(serializeUsing = IntegerSerializer.class, deserializeUsing = IntegerDeserializer.class)
    private Integer orderNum;
}
