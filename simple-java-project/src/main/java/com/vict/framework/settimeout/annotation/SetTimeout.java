package com.vict.framework.settimeout.annotation;

import java.lang.annotation.*;

/**
 * 标记期限服务
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD) //次注解只能作用于方法上
public @interface SetTimeout {
}
