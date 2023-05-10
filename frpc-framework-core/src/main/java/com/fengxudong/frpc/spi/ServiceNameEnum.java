package com.fengxudong.frpc.spi;

/**
 * @author feng xud
 */
public enum ServiceNameEnum {

    SERVICE_PROVIDER("ServiceProvider"),

    SERIALIZER("Serializer"),

    SERVICE_DISCOVERY("ServiceDiscovery"),

    TRANSPORT("FRpcTransport"),
    LOADBALANCE("LoadBalance");
    private final String name;

    ServiceNameEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}
