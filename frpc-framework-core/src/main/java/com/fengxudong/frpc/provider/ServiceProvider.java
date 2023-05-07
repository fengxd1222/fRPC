package com.fengxudong.frpc.provider;

import com.fengxudong.frpc.spi.FRpcSPI;

/**
 * @author feng xud
 */
@FRpcSPI
public interface ServiceProvider {

    void pulishService();

    Object getService();
}
