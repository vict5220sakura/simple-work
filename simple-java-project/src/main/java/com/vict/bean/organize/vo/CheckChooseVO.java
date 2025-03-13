package com.vict.bean.organize.vo;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.vict.framework.myannotation.MyDescription;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Map;

@Data
public class CheckChooseVO {
    @ApiModelProperty("id, [choosed, not]")
    private Map<String, String> map;

    @MyDescription("是否选中")
    @Getter
    @AllArgsConstructor
    public enum Choosed implements IEnum<String> {
        choosed("choosed", "选中"),
        not("not", "未选中"),
        ;
        private String value;
        private String name;
    }
}
