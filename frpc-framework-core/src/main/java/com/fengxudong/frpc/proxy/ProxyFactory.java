package com.fengxudong.frpc.proxy;

import com.fengxudong.frpc.config.FRpcProxyConfig;
import com.fengxudong.frpc.proxy.cglib.CglibProxy;
import com.fengxudong.frpc.proxy.jdk.JdkDynamicProxy;
import com.fengxudong.frpc.remote.FRpcTransport;

/**
 * @author feng xud
 */
public class ProxyFactory {

    public ProxyFactory() {
    }

    public static FRpcProxy createProxy(FRpcTransport fRpcTransport,FRpcProxyConfig fRpcProxyConfig){
        if(fRpcProxyConfig.getInterfaces()==null || fRpcProxyConfig.getInterfaces().length==0){
            return new JdkDynamicProxy(fRpcTransport,fRpcProxyConfig);
        }
        return new CglibProxy(fRpcTransport,fRpcProxyConfig);
    }
}
