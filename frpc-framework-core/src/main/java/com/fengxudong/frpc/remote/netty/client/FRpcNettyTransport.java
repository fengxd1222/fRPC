package com.fengxudong.frpc.remote.netty.client;

import com.fengxudong.frpc.constant.FRpcConstant;
import com.fengxudong.frpc.registry.ServiceDiscovery;
import com.fengxudong.frpc.remote.FRpcTransport;
import com.fengxudong.frpc.remote.domain.FRpcMessage;
import com.fengxudong.frpc.remote.domain.FRpcRequest;
import com.fengxudong.frpc.remote.domain.FRpcResponse;
import com.fengxudong.frpc.remote.netty.FRpcInitializer;
import com.fengxudong.frpc.remote.netty.codec.FRpcLengthFieldBasedFrameDecoder;
import com.fengxudong.frpc.remote.netty.codec.FRpcProtocolEncoder;
import com.fengxudong.frpc.serializer.Serializer;
import com.fengxudong.frpc.spi.FRpcLoader;
import com.fengxudong.frpc.spi.ServiceNameEnum;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author feng xud
 */
@Slf4j
public class FRpcNettyTransport implements FRpcTransport {

    private Bootstrap bootstrap;

    private EventLoopGroup childGroup;

    private ChannelHolder channelHolder;

    private ServiceDiscovery serviceDiscovery;

    private Serializer serializer;

    public FRpcNettyTransport() {
        //针对不同系统，启用不同的socket channel
        EventLoopGroupBuilder.Group<MultithreadEventLoopGroup> group = EventLoopGroupBuilder.build();
        bootstrap = new Bootstrap();
        childGroup = group.getC();
        bootstrap.group(childGroup)
                .channel(group.getChannelClass())
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new FRpcInitializer());

        this.channelHolder = ChannelHolder.getInstance();
        this.serviceDiscovery = FRpcLoader.getServiceLoader(ServiceDiscovery.class).getTarget(ServiceNameEnum.SERVICE_DISCOVERY.getName());
        this.serializer = FRpcLoader.getServiceLoader(Serializer.class).getTarget(ServiceNameEnum.SERIALIZER.getName());
    }
    @SneakyThrows
    @Override
    public Object doSendFrpcRequest(FRpcRequest fRpcRequest) {
        CompletableFuture<FRpcResponse<Object>> completableFuture = new CompletableFuture<>();
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(fRpcRequest);
        Channel channel = channelHolder.get(inetSocketAddress);
        if(channel==null){
            try {
                channel = initChannel(inetSocketAddress);
                channelHolder.set(inetSocketAddress,channel);
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        FRpcMessage fRpcMessage = FRpcMessage.builder()
                .messageType(FRpcConstant.MessageType.REQUEST_TYPE)
                .serializationType(serializer.getType())
                .data(fRpcRequest)
                .build();
        ResponseFutureHolder.getInstance().put(fRpcRequest.getRequestId(), completableFuture);
        channel.writeAndFlush(fRpcMessage).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("client send message: [{}]", fRpcMessage);
            } else {
                future.channel().close();
                completableFuture.completeExceptionally(future.cause());
                log.error("doSendFrpcRequest failed:", future.cause());
            }
        });
        return completableFuture.get();
    }

    private Channel initChannel(InetSocketAddress inetSocketAddress) throws ExecutionException, InterruptedException {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                if(log.isDebugEnabled()){
                    log.debug("The client has connected [{}] successful!", inetSocketAddress.toString());
                }
                completableFuture.complete(future.channel());
            } else {
                throw new IllegalStateException();
            }
        });
        return completableFuture.get();
    }
}
