package com.fengxudong.server;

import com.fengxudong.ApiModel;
import com.fengxudong.ApiService;
import com.fengxudong.frpc.annotation.FRpcService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author feng xud
 */
@Slf4j
@FRpcService(group = "apiGroup",version = "apiVersion")
public class ApiServiceImpl implements ApiService {
    @Override
    public ApiModel doService(ApiModel apiModel) {
        log.info("ApiServiceImpl doService begin");
        apiModel.setName("ApiServiceImpl");
        apiModel.setAge(1);
        return apiModel;
    }
}
