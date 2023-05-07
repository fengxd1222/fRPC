package com.fengxudong.client;

import com.fengxudong.frpc.annotation.FRpcService;

/**
 * @author feng xud
 */
@FRpcService
public class TestFrpcServiceImpl implements TestFrpcService{
    @Override
    public void sout() {
        System.out.println("TestFrpcServiceImpl");
    }
}
