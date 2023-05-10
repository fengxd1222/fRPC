package com.fengxudong.frpc.loadBalance;

import com.fengxudong.frpc.remote.domain.FRpcRequest;
import com.fengxudong.frpc.spi.FRpcSPI;

import java.util.List;

/**
 * @author feng xud
 */
@FRpcSPI
public interface LoadBalance {

    String obtainServiceUrl(List<String> urls, FRpcRequest fRpcRequest);
}
