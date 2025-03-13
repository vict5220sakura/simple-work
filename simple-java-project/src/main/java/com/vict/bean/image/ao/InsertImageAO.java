package com.vict.bean.image.ao;

import com.vict.entity.Image;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class InsertImageAO {

    @ApiModelProperty("url")
    private String url;

    @ApiModelProperty("svgCode")
    private String svgCode;

    @ApiModelProperty("base64Code")
    private String base64Code;

    public void check() {
        if (
            (url == null || "".equals(url.trim()))
            && (svgCode == null || "".equals(svgCode.trim()))
            && (base64Code == null || "".equals(base64Code.trim()))
        ) {
            throw new RuntimeException("url,svg,base64不能为空");
        }
    }

    public Image.ImageType getImageType(){
        if(this.url != null && !this.url.trim().equals("")){
            return Image.ImageType.ImageType_url;
        }else if(this.svgCode != null && !this.svgCode.trim().equals("")){
            return Image.ImageType.ImageType_svg;
        }else if(this.base64Code != null && !this.base64Code.trim().equals("")){
            return Image.ImageType.ImageType_base64;
        }else {
            return null;
        }
    }
}
