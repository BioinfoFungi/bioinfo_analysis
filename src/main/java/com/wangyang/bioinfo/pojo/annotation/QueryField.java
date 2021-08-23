package com.wangyang.bioinfo.pojo.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface QueryField {
    boolean keyWards() default false;
}
