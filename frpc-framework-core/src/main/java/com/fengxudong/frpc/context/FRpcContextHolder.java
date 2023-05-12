package com.fengxudong.frpc.context;

import com.fengxudong.frpc.remote.domain.FRpcRequest;
import com.fengxudong.frpc.remote.domain.FRpcResponse;

/**
 * @author feng xud
 */
public class FRpcContextHolder {
    private static final ThreadLocal<FRpcContext> MAIN_CONTEXT = ThreadLocal.withInitial(FRpcContext::new);

    private static final ThreadLocal<FRpcCustomizedContext> CUSTOMIZED_CONTEXT = ThreadLocal.withInitial(FRpcCustomizedContext::new);


    public static Boolean isConsumer(){
        return MAIN_CONTEXT.get().isConsumer();
    }

    public static Boolean isProvider(){
        return MAIN_CONTEXT.get().isProvider();
    }

    public static void confirmConsumer(){
        MAIN_CONTEXT.get().confirmConsumer();
    }

    public static void confirmProvider(){
        MAIN_CONTEXT.get().confirmProvider();
    }

    public static FRpcRequest getRequest(){
        return MAIN_CONTEXT.get().getRequest();
    }

    public static void setRequest(FRpcRequest request){
        MAIN_CONTEXT.get().setRequest(request);
    }

    public static FRpcResponse getResponse(){
        return MAIN_CONTEXT.get().getResponse();
    }

    public static void setResponse(FRpcResponse response){
        MAIN_CONTEXT.get().setResponse(response);
    }

    public static FRpcCustomizedContext getContext(){
        return CUSTOMIZED_CONTEXT.get();
    }

    public static void clear(){
        MAIN_CONTEXT.remove();
        CUSTOMIZED_CONTEXT.remove();
    }
}
