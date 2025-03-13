package com.vict.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.vict.framework.myannotation.MyDescription;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单(票)类型枚举
 */
@MyDescription("订单(票)类型枚举")
@Getter
@AllArgsConstructor
public enum TestEnmu implements IEnum<String> {
    TestEnmu_yes("TestEnmu_yes", "是"),
    TestEnmu_no("TestEnmu_no", "否"),
    ;
    private String value;
    private String name;

    @Override
    public String toString() {
        return "TestEnmu{" +
                "value='" + value + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
