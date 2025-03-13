package com.vict.framework.task.simpletask;

import java.lang.annotation.*;

/** 简化任务执行器 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD) // 次注解只能作用于方法上
public @interface SimpleTaskRun {
    /**
     * 重试延迟
     * 第二次根据第一次触发时间尝试等待[0]秒
     * 第三次尝试根据第二次触发时间等待[1]秒 */
    int[] failRetryTimeSeconds() default {};
}
