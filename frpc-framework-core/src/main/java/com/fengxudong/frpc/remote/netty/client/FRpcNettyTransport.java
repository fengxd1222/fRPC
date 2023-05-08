package com.fengxudong.frpc.remote.netty.client;

import com.fengxudong.frpc.remote.FRpcTransport;
import com.fengxudong.frpc.remote.domain.FRpcRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.MultithreadEventLoopGroup;

/**
 * @author feng xud
 */
public class FRpcNettyTransport implements FRpcTransport {

    private Bootstrap bootstrap;

    private EventLoopGroup childGroup;

    public FRpcNettyTransport() {
        //针对不同系统，启用不同的socket channel
        EventLoopGroupBuilder.Group<MultithreadEventLoopGroup> group = EventLoopGroupBuilder.build();
    }

    @Override
    public Object doSendFrpcRequest(FRpcRequest fRpcRequest) {

        return null;
    }
}
