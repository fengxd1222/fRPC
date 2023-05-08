package com.fengxudong.frpc.remote;

import com.fengxudong.frpc.remote.domain.FRpcRequest;
import com.fengxudong.frpc.spi.FRpcSPI;

/**
 * @author feng xud
 */
@FRpcSPI
public interface FRpcTransport {
    Object doSendFrpcRequest(FRpcRequest fRpcRequest);
}
