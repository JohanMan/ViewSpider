package com.johan.view.spider.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface EnableViewSpider {
    // 布局 R.id.xx xx就是布局名称
    String value() default "";
    // 生成的Spider类名
    String name() default "";
    // 字段是否使用id命名
    boolean origin() default false;
}
