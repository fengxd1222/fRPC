package com.fengxudong.frpc.loadBalance;

import com.fengxudong.frpc.remote.domain.FRpcRequest;

import java.util.List;
import java.util.Random;

/**
 * @author feng xud
 */
public class RandomLoadBalancer extends AbstractLoadBalance{
    @Override
    public String doObtain(List<String> urls, FRpcRequest fRpcRequest) {
        return urls.get(new Random().nextInt(urls.size()));
    }
}
