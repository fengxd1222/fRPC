package com.fengxudong.frpc.provider.zk;

import com.fengxudong.frpc.constant.FRpcConstant;
import com.fengxudong.frpc.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author feng xud
 */
@Slf4j
public class ZkSupport {

    private static final Map<String, List<String>> SERVICE_ADDRESS_MAP = new ConcurrentHashMap<>();
    private static final Set<String> REGISTERED_PATH = ConcurrentHashMap.newKeySet();

    private static CuratorFramework curatorFramework;

    private static final int BASE_SLEEP_TIME = 1000;
    private static final int MAX_RETRY_COUNT = 10;

    public static void registryService(String servicePath, String ip) {
        try {
            if (curatorFramework == null) {
                getOrCreate();
            }
            //父目录为永久节点
            if (!REGISTERED_PATH.contains(servicePath) && curatorFramework.checkExists().forPath(servicePath) == null) {
                log.info("ZkSupport registryService servicePath {}", servicePath);
                //path /frpc/com.fengxudong.ApiServiceapiGroupapiVersion/192.168.80.1:8888 需要保证ip节点是临时node
                curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(servicePath);
                log.info("ZkSupport registryService success");
                REGISTERED_PATH.add(servicePath);
            }
            servicePath += ip;
            //ip node为临时节点
            if (!REGISTERED_PATH.contains(servicePath) && curatorFramework.checkExists().forPath(servicePath) == null) {
                log.info("ZkSupport registryService servicePath {}", servicePath);
                curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath(servicePath);
                REGISTERED_PATH.add(servicePath);
                log.info("ZkSupport registryService success");
            }
            if (log.isDebugEnabled()) {
                log.debug("ZkSupport registryService has already exists");
            }
        } catch (Exception e) {
            log.error("ZkSupport registryService error");
            e.printStackTrace();
        }
    }

    public static List<String> listChildrenNodes(String fRpcServiceName) {
        if (SERVICE_ADDRESS_MAP.containsKey(fRpcServiceName)) {
            return SERVICE_ADDRESS_MAP.get(fRpcServiceName);
        }
        if (curatorFramework == null) {
            getOrCreate();
        }
        String servicePath = FRpcConstant.ZkConfig.ZK_ROOT_PATH + "/" + fRpcServiceName;
        try {
            List<String> results = curatorFramework.getChildren().forPath(servicePath);
            SERVICE_ADDRESS_MAP.put(fRpcServiceName, results);
            addWatcher(fRpcServiceName);
            return results;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    private static void addWatcher(String fRpcServiceName) {
        String servicePath = FRpcConstant.ZkConfig.ZK_ROOT_PATH + "/" + fRpcServiceName;
        PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework, servicePath, true);
        PathChildrenCacheListener pathChildrenCacheListener = (curatorFramework, pathChildrenCacheEvent) -> {
            List<String> serviceAddresses = curatorFramework.getChildren().forPath(servicePath);
            SERVICE_ADDRESS_MAP.put(fRpcServiceName, serviceAddresses);
        };
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        try {
            pathChildrenCache.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static CuratorFramework getOrCreate() {
        if (curatorFramework != null && curatorFramework.getState() == CuratorFrameworkState.STARTED) {
            return curatorFramework;
        }
        Properties properties = PropertiesUtil.resourceRead(FRpcConstant.ZkConfig.ZK_CONFIG_CLASS_PATH, FRpcConstant.ZkConfig.ZK_CONFIG_FILE_NAME);
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRY_COUNT);
        String zookeeperAddress = properties != null && properties.getProperty(FRpcConstant.ZkConfig.ZK_ADDRESS) != null ? properties.getProperty(FRpcConstant.ZkConfig.ZK_ADDRESS) : FRpcConstant.ZkConfig.DEFAULT_ZOOKEEPER_ADDRESS;
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
        return curatorFramework;
    }

    public static void removeNodes(InetSocketAddress inetSocketAddress) {
        try {
            for (String path : REGISTERED_PATH) {
                if (path.endsWith(inetSocketAddress.toString())) {
                    curatorFramework.delete().forPath(path);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
