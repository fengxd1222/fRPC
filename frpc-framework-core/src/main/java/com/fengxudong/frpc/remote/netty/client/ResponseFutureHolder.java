package com.fengxudong.frpc.remote.netty.client;

import com.fengxudong.frpc.remote.domain.FRpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author feng xud
 */
@Slf4j
public class ResponseFutureHolder {

    private final Map<String, CompletableFuture<FRpcResponse<Object>>> completableFutureMap;

    private volatile static ResponseFutureHolder responseFutureHolder=null;
    private ResponseFutureHolder(){
        this.completableFutureMap = new ConcurrentHashMap<>();
    }

    public static ResponseFutureHolder getInstance(){
        if(responseFutureHolder==null){
            synchronized (ResponseFutureHolder.class){
                if(responseFutureHolder==null){
                    responseFutureHolder = new ResponseFutureHolder();
                }
            }
        }
        return responseFutureHolder;
    }

    public void put(String requestId,CompletableFuture<FRpcResponse<Object>> future){
        completableFutureMap.put(requestId,future);
    }

    public void complete(FRpcResponse<Object> fRpcResponse){
        CompletableFuture<FRpcResponse<Object>> completableFuture = completableFutureMap.remove(fRpcResponse.getRequestId());
        if(completableFuture!=null){
            completableFuture.complete(fRpcResponse);
        }else {
            if(log.isWarnEnabled()){
                log.warn("无效请求");
            }
        }
    }
}
