package com.fengxudong.frpc.annotation;

import java.lang.annotation.*;

/**
 * @author feng xud
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
@Documented
public @interface FRpcReference {

    String group() default "";

    String version() default "";
}
