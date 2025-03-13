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
@TableName("chinese_poetry")
public class ChinesePoetry {

    @TableId
    @ApiModelProperty("id")
    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    private Long id;

    @ApiModelProperty("标题")
    @TableField("title")
    private String title;

    @ApiModelProperty("作者")
    @TableField("author")
    private String author;

    @ApiModelProperty("内容")
    @TableField("paragraphs")
    private String paragraphs;

    @TableField("classify_id")
    @ApiModelProperty("类别id")
    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    private Long classifyId;

    @ApiModelProperty("排序")
    @TableField("order_num")
    @JSONField(serializeUsing = IntegerSerializer.class, deserializeUsing = IntegerDeserializer.class)
    private Integer orderNum;

    @ApiModelProperty("描述")
    @TableField("cdesc")
    private String cdesc;
}
