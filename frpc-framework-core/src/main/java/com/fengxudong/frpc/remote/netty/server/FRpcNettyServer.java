package com.fengxudong.frpc.remote.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.MultithreadEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

/**
 * @author feng xud
 */
@Slf4j
public class FRpcNettyServer {
    public static final int SERVER_PORT = 8888;

    public void run(){
        EventLoopGroupBuilder.Group<MultithreadEventLoopGroup, MultithreadEventLoopGroup> group = new EventLoopGroupBuilder().build();
        MultithreadEventLoopGroup parentGroup = group.getP();
        MultithreadEventLoopGroup childGroup = group.getC();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(parentGroup,childGroup)
                    .channel(group.getChannelClass())
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childOption(ChannelOption.TCP_NODELAY,true);
//                    .childOption(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator());

            EventLoopGroupBuilder.addHandler(serverBootstrap);

            //ch.pipeline().addLast(new FixedLengthFrameDecoder(10));
            EventLoopGroupBuilder.addChildHandler(serverBootstrap);

            ChannelFuture channelFuture = serverBootstrap.bind(SERVER_PORT).sync();
            channelFuture.channel().closeFuture().sync();
        }catch (InterruptedException e) {
            log.error("server error", e);
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
            log.info("netty server closed");
        }


    }
}
