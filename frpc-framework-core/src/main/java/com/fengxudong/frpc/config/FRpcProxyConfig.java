package com.fengxudong.frpc.config;

import lombok.*;

/**
 * @author feng xud
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class FRpcProxyConfig {
    private FRpcServiceConfig fRpcServiceConfig;

    private Class<?>[] interfaces;

    private Class<?> targetClass;


    public void placeClass(Class<?> targetClass){
        if(targetClass.isInterface()){
            interfaces = new Class[]{targetClass};
            return;
        }
        this.targetClass = targetClass;
    }
}
