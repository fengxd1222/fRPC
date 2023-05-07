package com.fengxudong.frpc.annotation;

import com.fengxudong.frpc.scan.FRpcScannerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author feng xud
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(FRpcScannerRegistrar.class)
public @interface FRpcScan {
    String[] basePackages() default "";
}
