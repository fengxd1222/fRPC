package com.fengxudong.frpc.registry.zk;

import com.fengxudong.frpc.enums.FRpcErrorEnum;
import com.fengxudong.frpc.loadBalance.LoadBalance;
import com.fengxudong.frpc.provider.zk.ZkSupport;
import com.fengxudong.frpc.registry.ServiceDiscovery;
import com.fengxudong.frpc.remote.domain.FRpcRequest;
import com.fengxudong.frpc.spi.FRpcLoader;
import com.fengxudong.frpc.spi.ServiceNameEnum;
import org.springframework.util.CollectionUtils;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author feng xud
 */
public class ZkServiceDiscovery implements ServiceDiscovery {

    private final LoadBalance loadBalance;
    public ZkServiceDiscovery() {
        loadBalance = FRpcLoader.getServiceLoader(LoadBalance.class).getTarget(ServiceNameEnum.LOADBALANCE.getName());
    }

    @Override
    public InetSocketAddress lookupService(FRpcRequest fRpcRequest) {
        String rpcServiceName = fRpcRequest.getRpcServiceName();
        List<String> urls = ZkSupport.listChildrenNodes(rpcServiceName);
        if(CollectionUtils.isEmpty(urls)){
            throw new RuntimeException(FRpcErrorEnum.SERVICE_NOT_FOUND.getMessage());
        }
        String url = loadBalance.obtainServiceUrl(urls, fRpcRequest);
        String[] strs = url.split(":");
        return new InetSocketAddress(strs[0],Integer.parseInt(strs[1]));
    }
}
