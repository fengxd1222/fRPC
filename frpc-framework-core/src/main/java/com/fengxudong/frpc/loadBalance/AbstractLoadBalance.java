package com.fengxudong.frpc.loadBalance;

import com.fengxudong.frpc.remote.domain.FRpcRequest;

import java.util.List;

/**
 * @author feng xud
 */
public abstract class AbstractLoadBalance implements LoadBalance {

    @Override
    public String obtainServiceUrl(List<String> urls, FRpcRequest fRpcRequest) {
        if (urls.size() == 1) {
            return urls.get(0);
        }
        return doObtain(urls, fRpcRequest);
    }

    public abstract String doObtain(List<String> urls, FRpcRequest fRpcRequest);
}
