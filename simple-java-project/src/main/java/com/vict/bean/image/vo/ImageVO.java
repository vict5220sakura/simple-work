package com.vict.bean.image.vo;

import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson.annotation.JSONField;
import com.vict.entity.Image;
import com.vict.framework.fastjsonserializer.EnumDeserializer;
import com.vict.framework.fastjsonserializer.EnumSerializer;
import com.vict.framework.fastjsonserializer.LongDeserializer;
import com.vict.framework.fastjsonserializer.LongSerializer;
import com.vict.framework.utils.QiNiuUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Optional;

@Data
public class ImageVO {

    @JSONField(serialize = false, deserialize = false)
    private Image image;

    @ApiModelProperty("id")
    @JSONField(serializeUsing = LongSerializer.class, deserializeUsing = LongDeserializer.class)
    public Long getId(){
        return Optional.ofNullable(image).map(o-> o.getId()).orElse(null);
    }

    @ApiModelProperty("别名")
    public String getAttname(){
        return Optional.ofNullable(image).map(o-> o.getAttname()).orElse(null);
    }

    @ApiModelProperty("图片链接")
    public String getUrl(){
        String url = Optional.ofNullable(image).map(o -> o.getUrl()).map(o-> o.trim())
                .filter(o-> !o.equals("")).orElse(null);
        if(url == null){
            return url;
        }
        String oldAttname = QiNiuUtils.getAttname(url);
        String attname = Optional.ofNullable(image).map(o -> o.getAttname()).map(o -> o.trim()).filter(o -> !o.equals("")).orElse(null);
        String newUrl = url.replace(oldAttname, URLUtil.encode(attname));
        return newUrl;
    }

    @ApiModelProperty("svgCode")
    public String getSvgCode(){
        return Optional.ofNullable(image).map(o-> o.getSvgCode()).orElse(null);
    }

    @ApiModelProperty("base64Code")
    public String getBase64Code(){
        return Optional.ofNullable(image).map(o-> o.getBase64Code()).orElse(null);
    }

    @JSONField(serializeUsing = EnumSerializer.class, deserializeUsing = EnumDeserializer.class)
    @ApiModelProperty("图片类型")
    public Image.ImageType getImageType(){
        return Optional.ofNullable(image).map(o-> o.getImageType()).orElse(null);
    }
}
