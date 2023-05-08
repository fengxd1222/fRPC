package com.fengxudong.frpc.proxy.cglib;

import com.fengxudong.frpc.config.FRpcProxyConfig;
import com.fengxudong.frpc.config.FRpcServiceConfig;
import com.fengxudong.frpc.proxy.FRpcProxy;
import com.fengxudong.frpc.remote.FRpcTransport;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author feng xud
 */
public class CglibProxy implements FRpcProxy, MethodInterceptor, Serializable {

    private final FRpcServiceConfig fRpcServiceConfig;
    private final FRpcTransport fRpcTransport;

    private Class<?> targetClass;
    public CglibProxy(FRpcTransport fRpcTransport,FRpcProxyConfig fRpcProxyConfig){
        this.fRpcTransport = fRpcTransport;
        this.fRpcServiceConfig = fRpcProxyConfig.getFRpcServiceConfig();
        this.targetClass = fRpcProxyConfig.getTargetClass();
    }

    @Override
    public Object getProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(targetClass);
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        return null;
    }
}
