package com.vict.bean.image.ao;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.vict.entity.Image;
import com.vict.framework.bean.PageRequest;
import com.vict.framework.fastjsonserializer.EnumDeserializer;
import com.vict.framework.fastjsonserializer.EnumSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SelectImageAO extends PageRequest {
    @ApiModelProperty("别名")
    private String attname;

    @ApiModelProperty("图片类型")
    @TableField("image_type")
    @JSONField(serializeUsing = EnumSerializer.class, deserializeUsing = EnumDeserializer.class)
    private Image.ImageType imageType;
}
