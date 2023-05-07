package com.fengxudong.frpc.annotation;

import java.lang.annotation.*;

/**
 * Used to mark the provider implementation class,
 * which publishes the service to the registry for consumption by consumers.
 * @author feng xud
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface FRpcService {

    String group() default "";

    String version() default "";
}
