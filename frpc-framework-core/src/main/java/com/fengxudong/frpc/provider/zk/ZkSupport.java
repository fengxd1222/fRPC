package com.fengxudong.frpc.provider.zk;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author feng xud
 */
@Slf4j
public class ZkSupport {

    private static final Map<String, List<String>> SERVICE_ADDRESS_MAP = new ConcurrentHashMap<>();
    private static final Set<String> REGISTERED_PATH = ConcurrentHashMap.newKeySet();
    public static void registryService(CuratorFramework curatorFramework, String servicePath) {

        try {
            if(!REGISTERED_PATH.contains(servicePath) && curatorFramework.checkExists().forPath(servicePath)==null){
                curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(servicePath);
                log.info("ZkSupport registryService success");
                return;
            }
            if(log.isDebugEnabled()){
                log.debug("ZkSupport registryService has already exists");
            }
        }catch (Exception e){
            log.error("ZkSupport registryService error");
            e.printStackTrace();
        }

    }
}
