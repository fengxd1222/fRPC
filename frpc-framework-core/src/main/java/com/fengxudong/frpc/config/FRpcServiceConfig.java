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
public class FRpcServiceConfig {
    private String group;

    private String version;

    private Object service;


    public String getRpcServiceName() {
        return this.getServiceName() + this.getGroup() + this.getVersion();
    }

    public String getServiceName() {
        return this.service.getClass().getInterfaces()[0].getCanonicalName();
    }
}
