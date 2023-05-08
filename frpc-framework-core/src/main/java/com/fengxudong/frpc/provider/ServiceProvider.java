package com.fengxudong.frpc.provider;

import com.fengxudong.frpc.config.FRpcServiceConfig;
import com.fengxudong.frpc.spi.FRpcSPI;

import java.net.InetSocketAddress;

/**
 * @author feng xud
 */
@FRpcSPI
public interface ServiceProvider {

    void publishService(FRpcServiceConfig fRpcServiceConfig);


    Object getService(String fRpcServiceName);
}
