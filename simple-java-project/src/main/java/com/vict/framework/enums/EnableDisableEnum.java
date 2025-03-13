package com.vict.framework.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnableDisableEnum implements IEnum<String> {
    enabled("enabled", "启用"),
    disable("disable", "禁用"),
    ;
    private String value;
    private String name;

    @Override
    public String toString() {
        return "DisableAndEnable2Enum{" +
                "value='" + value + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
