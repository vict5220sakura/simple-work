package com.vict.framework.task.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 标记任务执行器
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface TaskConsumerConfiguration {
    String value() default "";

    /**
     * 自定义类型
     */
    String type();

    /**
     * 每个社区同一时间执行数量
     */
    int sameTimeWorkNum();

    /**
     * 重试延迟
     * 第二次根据第一次触发时间尝试等待[0]秒
     * 第三次尝试根据第二次触发时间等待[1]秒 */
    int[] failRetryTimeSeconds() default {};
}
