package com.fengxudong.frpc.provider.zk;

import com.fengxudong.frpc.config.FRpcServiceConfig;
import com.fengxudong.frpc.constant.FRpcConstant;
import com.fengxudong.frpc.enums.FRpcErrorEnum;
import com.fengxudong.frpc.provider.ServiceProvider;
import com.fengxudong.frpc.remote.netty.server.FRpcNettyServer;
import com.fengxudong.frpc.util.PropertiesUtil;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author feng xud
 */
public class ZkServiceProvider implements ServiceProvider {


    private final Map<String,Object> fRpcServiceMap;

    private final Set<String> fRpcServiceNames;
    public ZkServiceProvider() {
        fRpcServiceMap = new ConcurrentHashMap<>();
        fRpcServiceNames = ConcurrentHashMap.newKeySet();
    }

    @Override
    public void publishService(FRpcServiceConfig fRpcServiceConfig) {
        if(!fRpcServiceNames.contains(fRpcServiceConfig.getRpcServiceName())){
            fRpcServiceNames.add(fRpcServiceConfig.getRpcServiceName());
            fRpcServiceMap.put(fRpcServiceConfig.getRpcServiceName(),fRpcServiceConfig.getService());
        }
        try {
            String servicePath = FRpcConstant.ZkConfig.ZK_ROOT_PATH + "/" + fRpcServiceConfig.getRpcServiceName()  +new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), FRpcNettyServer.SERVER_PORT).toString();
            ZkSupport.registryService(servicePath);
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    @Override
    public Object getService(String fRpcServiceName) {
        if(!fRpcServiceMap.containsKey(fRpcServiceName)){
            throw new RuntimeException(FRpcErrorEnum.SERVICE_NOT_FOUND.getMessage());
        }
        return fRpcServiceMap.get(fRpcServiceName);
    }



}
