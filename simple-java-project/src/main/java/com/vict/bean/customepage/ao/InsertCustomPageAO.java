package com.vict.bean.customepage.ao;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class InsertCustomPageAO {
    @ApiModelProperty("页面名称")
    private String customName;


    @ApiModelProperty("页面内容")
    private String pageValue;

    public void check(){
        if (customName == null || customName.equals("")) {
            throw new RuntimeException("页面名称不能为空");
        }
    }
}
