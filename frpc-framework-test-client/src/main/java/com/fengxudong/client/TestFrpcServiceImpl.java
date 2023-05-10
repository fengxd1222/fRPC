package com.fengxudong.client;

import com.fengxudong.ApiModel;
import com.fengxudong.ApiService;
import com.fengxudong.frpc.annotation.FRpcReference;
import com.fengxudong.frpc.annotation.FRpcService;
import org.springframework.stereotype.Component;

/**
 * @author feng xud
 */
@Component
public class TestFrpcServiceImpl implements TestFrpcService{

    @FRpcReference(group = "apiGroup",version = "apiVersion")
    ApiService apiService;
    @Override
    public void sout() {
        System.out.println("TestFrpcServiceImpl");
        ApiModel testFrpcServiceImpl = apiService.doService(new ApiModel("TestFrpcServiceImpl", 0));
        System.out.println(testFrpcServiceImpl);
    }
}
