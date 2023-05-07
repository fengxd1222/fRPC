package com.fengxudong.frpc.provider.zk;

import com.fengxudong.frpc.provider.ServiceProvider;

/**
 * @author feng xud
 */
public class ZkServiceProvider implements ServiceProvider {
    @Override
    public void pulishService() {
        System.out.println("ZkServiceProvider pulishService");
    }

    @Override
    public Object getService() {
        return null;
    }
}
