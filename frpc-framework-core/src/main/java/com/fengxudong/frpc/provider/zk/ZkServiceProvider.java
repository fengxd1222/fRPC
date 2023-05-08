package com.fengxudong.frpc.provider.zk;

import com.fengxudong.frpc.config.FRpcServiceConfig;
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



    private static CuratorFramework curatorFramework;

    private static final String ZK_CONFIG_FILE_NAME = "frpc.properties";
    private static final String ZK_CONFIG_CLASS_PATH = "classpath*:frpc.properties";
    private static final String ZK_ADDRESS = "frpc.zookeeper.address";
    private static final String DEFAULT_ZOOKEEPER_ADDRESS = "127.0.0.1:2181";

    private static final String ZK_ROOT_PATH = "/frpc";
    private static final int BASE_SLEEP_TIME = 1000;
    private static final int MAX_RETRY_COUNT = 10;

    private final Map<String,Object> fRpcServiceMap;

    private final Set<String> fRpcServiceNames;
    public ZkServiceProvider() {
        fRpcServiceMap = new ConcurrentHashMap<>();
        fRpcServiceNames = ConcurrentHashMap.newKeySet();
    }

    @Override
    public void publishService(FRpcServiceConfig fRpcServiceConfig) {
        if(curatorFramework==null){
            getOrCreate();
        }
        if(!fRpcServiceNames.contains(fRpcServiceConfig.getRpcServiceName())){
            fRpcServiceNames.add(fRpcServiceConfig.getRpcServiceName());
            fRpcServiceMap.put(fRpcServiceConfig.getRpcServiceName(),fRpcServiceConfig.getService());
        }
        try {
            String servicePath = ZK_ROOT_PATH + "/" + fRpcServiceConfig.getRpcServiceName() + new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), FRpcNettyServer.SERVER_PORT).toString();
            ZkSupport.registryService(curatorFramework,servicePath);
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    @Override
    public Object getService(String fRpcServiceName) {
        return null;
    }


    private void getOrCreate(){
        if(curatorFramework!=null && curatorFramework.getState()== CuratorFrameworkState.STARTED){
            return;
        }
        Properties properties = PropertiesUtil.resourceRead(ZK_CONFIG_CLASS_PATH,ZK_CONFIG_FILE_NAME);
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRY_COUNT);
        String zookeeperAddress = properties != null && properties.getProperty(ZK_ADDRESS) != null ? properties.getProperty(ZK_ADDRESS) : DEFAULT_ZOOKEEPER_ADDRESS;
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(zookeeperAddress)
                .retryPolicy(retryPolicy)
                .build();
        curatorFramework.start();
        try {
            // wait 30s until connect to the zookeeper
            if (!curatorFramework.blockUntilConnected(30, TimeUnit.SECONDS)) {
                throw new RuntimeException("Connection to Zookeeper timed out.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
