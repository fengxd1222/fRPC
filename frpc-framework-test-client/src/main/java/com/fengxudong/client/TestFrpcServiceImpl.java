package com.fengxudong.client;

import com.fengxudong.frpc.annotation.FRpcService;

/**
 * @author feng xud
 */
@FRpcService(group = "test",version = "1.0")
public class TestFrpcServiceImpl implements TestFrpcService{
    @Override
    public void sout() {
        System.out.println("TestFrpcServiceImpl");
    }
}
