package com.fengxudong.frpc.proxy.jdk;

import com.fengxudong.frpc.config.FRpcProxyConfig;
import com.fengxudong.frpc.config.FRpcServiceConfig;
import com.fengxudong.frpc.proxy.FRpcProxy;
import com.fengxudong.frpc.remote.FRpcTransport;
import com.fengxudong.frpc.remote.domain.FRpcRequest;
import com.fengxudong.frpc.remote.domain.FRpcResponse;
import com.sun.istack.internal.NotNull;
import org.springframework.util.ClassUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * @author feng xud
 */
public class JdkDynamicProxy implements FRpcProxy, InvocationHandler, Serializable {

    private final FRpcTransport fRpcTransport;

    private final FRpcServiceConfig fRpcServiceConfig;

    private Class<?>[] interfaces;

    public JdkDynamicProxy(@NotNull FRpcTransport fRpcTransport, @NotNull FRpcProxyConfig fRpcProxyConfig) {
        this.fRpcTransport = fRpcTransport;
        this.fRpcServiceConfig = fRpcProxyConfig.getFRpcServiceConfig();
        this.interfaces = fRpcProxyConfig.getInterfaces();
    }

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(ClassUtils.getDefaultClassLoader(),interfaces,this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //send FRpcRequest to server
        FRpcRequest fRpcRequest = FRpcRequest.builder().
                classOrInterfaceName(method.getDeclaringClass().getName())
                        .methodName(method.getName())
                                .parameters(args)
                                        .paramTypes(method.getParameterTypes())
                                                .group(fRpcServiceConfig.getGroup())
                                                        .version(fRpcServiceConfig.getVersion())
                                                            .requestId(UUID.randomUUID().toString())
                                                                .build();

        FRpcResponse<Object> fRpcResponse = (FRpcResponse<Object>) fRpcTransport.doSendFrpcRequest(fRpcRequest);
        return fRpcResponse.getData();
    }
}
