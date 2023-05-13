package com.fengxudong.frpc.context;

import com.fengxudong.frpc.remote.domain.FRpcRequest;
import com.fengxudong.frpc.remote.domain.FRpcResponse;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

/**
 * rpc上下文，存储当前线程的请求信息
 * @author feng xud
 */

@ToString
public class FRpcContext {
    private static final String CONSUMER_KEY="CONSUMER";
    private static final String PROVIDER_KEY="PROVIDER";

    private Map<Object,Object> mainAttrs;

    public FRpcContext() {
        this.mainAttrs = new HashMap<>();
    }

    public Object get(String key){
        return mainAttrs.get(key);
    }

    public FRpcContext set(String key,Object value){
        mainAttrs.put(key,value);
        return this;
    }

    public <T> T get(Class<T> type){
        return (T) mainAttrs.get(type);
    }

    public <T> FRpcContext set(Class<T> type,T value){
        mainAttrs.put(type,value);
        return this;
    }


    public FRpcRequest getRequest(){
        return get(FRpcRequest.class);
    }

    public FRpcContext setRequest(FRpcRequest fRpcRequest){
        return set(FRpcRequest.class,fRpcRequest);
    }

    public FRpcResponse getResponse(){
        return get(FRpcResponse.class);
    }

    public FRpcContext setResponse(FRpcResponse fRpcResponse){
        return set(FRpcResponse.class,fRpcResponse);
    }

    public Boolean isConsumer(){
        return mainAttrs.containsKey(CONSUMER_KEY)&&(Boolean) mainAttrs.get(CONSUMER_KEY);
    }

    public Boolean isProvider(){
        return mainAttrs.containsKey(PROVIDER_KEY)&&(Boolean) mainAttrs.get(PROVIDER_KEY);
    }

    public FRpcContext confirmConsumer(){
        mainAttrs.put(CONSUMER_KEY,Boolean.TRUE);
        return this;
    }

    public FRpcContext confirmProvider(){
        mainAttrs.put(PROVIDER_KEY,Boolean.TRUE);
        return this;
    }
}
