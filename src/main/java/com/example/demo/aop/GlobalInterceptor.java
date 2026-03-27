package com.example.demo.aop;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})

public @interface GlobalInterceptor {
    boolean checkLogin() default true;
    boolean autoFill() default true;

}
