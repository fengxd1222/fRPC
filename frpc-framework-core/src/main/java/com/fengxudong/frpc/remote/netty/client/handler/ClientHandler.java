package com.fengxudong.frpc.remote.netty.client.handler;

import com.fengxudong.frpc.constant.FRpcConstant;
import com.fengxudong.frpc.remote.domain.FRpcMessage;
import com.fengxudong.frpc.remote.domain.FRpcResponse;
import com.fengxudong.frpc.remote.netty.client.ChannelHolder;
import com.fengxudong.frpc.remote.netty.client.ResponseFutureHolder;
import com.fengxudong.frpc.serializer.Serializer;
import com.fengxudong.frpc.spi.FRpcLoader;
import com.fengxudong.frpc.spi.ServiceNameEnum;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author feng xud
 */
@Slf4j
public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FRpcMessage) {
            FRpcMessage fRpcMessage = (FRpcMessage) msg;
            log.info(" client read msg {}", fRpcMessage);
            if(fRpcMessage.getMessageType()==FRpcConstant.MessageType.RESPONSE_TYPE){
                ResponseFutureHolder.getInstance().complete((FRpcResponse<Object>) fRpcMessage.getData());
            }
        }
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            log.info("client heartbeat");
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                log.info("client IdleStateEvent {}", ctx.channel().remoteAddress());
                Channel channel = ctx.channel();
                FRpcMessage fRpcMessage = FRpcMessage.builder()
                        .messageType(FRpcConstant.MessageType.HEARTBEAT_REQUEST_TYPE)
                        .serializationType(FRpcLoader.getServiceLoader(Serializer.class).getTarget(ServiceNameEnum.SERIALIZER.getName()).getType())
                        .build();
                channel.writeAndFlush(fRpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
