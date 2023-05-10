package com.fengxudong.frpc.loadBalance;

import com.fengxudong.frpc.remote.domain.FRpcRequest;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * simple RoundRobinLoadBalancer
 * @author feng xud
 */
public class RoundRobinLoadBalancer extends AbstractLoadBalance{

    private final Map<String, AtomicLong> counterMap;

    public RoundRobinLoadBalancer(){
        this.counterMap = new ConcurrentHashMap<>();
    }

    @Override
    public String doObtain(List<String> urls, FRpcRequest fRpcRequest) {
        if(!counterMap.containsKey(fRpcRequest.getRpcServiceName())){
            counterMap.put(fRpcRequest.getRpcServiceName(),new AtomicLong(0));
        }
        AtomicLong atomicLong = counterMap.get(fRpcRequest.getRpcServiceName());
        if(atomicLong.get() >= (Integer.MAX_VALUE>>1)){
            atomicLong.set(0);
        }
        int next = Math.toIntExact((atomicLong.incrementAndGet() + 1) % urls.size());
        return urls.get(next);
    }
}
