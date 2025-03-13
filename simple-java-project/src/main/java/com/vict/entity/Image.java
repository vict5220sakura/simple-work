package com.vict.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.vict.framework.fastjsonserializer.*;
import com.vict.framework.myannotation.MyDescription;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@ApiModel
@TableName("image")
public class Image {

    @TableId
    @ApiModelProperty("id")
    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    private Long id;

    @ApiModelProperty("url")
    @TableField("url")
    private String url;

    @ApiModelProperty("别名")
    @TableField("attname")
    private String attname;

    @ApiModelProperty("svgCode")
    @TableField("svg_code")
    private String svgCode;

    @ApiModelProperty("base64Code")
    @TableField("base64_code")
    private String base64Code;

    @ApiModelProperty("排序")
    @TableField("order_num")
    @JSONField(serializeUsing = IntegerSerializer.class, deserializeUsing = IntegerDeserializer.class)
    private Integer orderNum;

    @MyDescription("图片类型")
    @Getter
    @AllArgsConstructor
    public enum ImageType implements IEnum<String> {
        ImageType_url("url", "url"),
        ImageType_svg("svg", "svg"),
        ImageType_base64("base64", "base64"),
        ;
        private String value;
        private String name;

        @Override
        public String toString() {
            return "ImageType{" +
                    "value='" + value + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    @ApiModelProperty("图片类型")
    @TableField("image_type")
    @JSONField(serializeUsing = EnumSerializer.class, deserializeUsing = EnumDeserializer.class)
    private ImageType imageType;
}
