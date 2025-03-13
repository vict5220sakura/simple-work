package com.vict.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.vict.framework.fastjsonserializer.DecimalDeserializer;
import com.vict.framework.fastjsonserializer.DecimalSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.vict.framework.fastjsonserializer.LongDeserializer;
import com.vict.framework.fastjsonserializer.LongSerializer;

import java.math.BigDecimal;

@Data
@TableName(value ="testtable")
public class Testtable {

    @TableId
    @ApiModelProperty("id")
    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    private Long id;


    @ApiModelProperty("money")
    @JSONField(serializeUsing = DecimalSerializer.class, deserializeUsing = DecimalDeserializer.class)
    private BigDecimal money;
}
