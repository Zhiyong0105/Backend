package org.springframe.backend.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessLimit {
    int seconds() ;
    int maxCount() ;
    String msg() default "Access limit";
}
