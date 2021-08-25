package com.wangyang.bioinfo.pojo.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Authorize {
    String value() default "";
    String role() default "ADMIN";
}
