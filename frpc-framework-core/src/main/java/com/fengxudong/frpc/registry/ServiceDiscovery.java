package com.fengxudong.frpc.registry;

import com.fengxudong.frpc.remote.domain.FRpcRequest;
import com.fengxudong.frpc.spi.FRpcSPI;

import java.net.InetSocketAddress;

/**
 * @author feng xud
 */
@FRpcSPI
public interface ServiceDiscovery {
    InetSocketAddress lookupService(FRpcRequest fRpcRequest);
}
