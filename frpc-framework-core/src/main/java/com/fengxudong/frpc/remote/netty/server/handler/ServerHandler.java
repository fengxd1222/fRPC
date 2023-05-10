package com.fengxudong.frpc.remote.netty.server.handler;

import com.fengxudong.frpc.constant.FRpcConstant;
import com.fengxudong.frpc.enums.FRpcResponseEnum;
import com.fengxudong.frpc.provider.ServiceProvider;
import com.fengxudong.frpc.remote.domain.FRpcMessage;
import com.fengxudong.frpc.remote.domain.FRpcRequest;
import com.fengxudong.frpc.remote.domain.FRpcResponse;
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author feng xud
 */
@Slf4j
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private final ServiceProvider serviceProvider;

    public ServerHandler() {
        this.serviceProvider = FRpcLoader.getServiceLoader(ServiceProvider.class).getTarget(ServiceNameEnum.SERVICE_PROVIDER.getName());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("ServerHandler receive msg {} ",msg);
        if (msg instanceof FRpcMessage) {
            FRpcMessage fRpcMessage = (FRpcMessage) msg;
            FRpcMessage fRpcMessageForRet = FRpcMessage.builder()
                    .serializationType(fRpcMessage.getSerializationType())
                    .build();
            if (fRpcMessage.getMessageType() == FRpcConstant.MessageType.HEARTBEAT_REQUEST_TYPE) {
                fRpcMessageForRet.setMessageType(FRpcConstant.MessageType.HEARTBEAT_RESPONSE_TYPE);
            } else {
                FRpcRequest fRpcRequest = (FRpcRequest) fRpcMessage.getData();
                Object service = serviceProvider.getService(fRpcRequest.getRpcServiceName());
                Object result = invokeTargetMethod(service, fRpcRequest);
                fRpcMessageForRet.setMessageType(FRpcConstant.MessageType.RESPONSE_TYPE);
                Channel channel = ctx.channel();
                if (channel.isActive() && channel.isWritable()) {
                    FRpcResponse<Object> ok = FRpcResponse.ok(fRpcRequest.getRequestId(), result);
                    fRpcMessageForRet.setData(ok);
                }else {
                    fRpcMessageForRet.setData(FRpcResponse.error(FRpcResponseEnum.ERROR.getMessage(), FRpcResponseEnum.ERROR, fRpcRequest.getRequestId()));
                }
            }
            ctx.writeAndFlush(fRpcMessageForRet).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        }else {
            ReferenceCountUtil.release(msg);
        }
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                log.info("idle check happen, so close the connection");
//                ctx.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    private Object invokeTargetMethod(Object service, FRpcRequest fRpcRequest) {
        Object result;
        try {
            Method method = service.getClass().getMethod(fRpcRequest.getMethodName(), fRpcRequest.getParamTypes());
            result = method.invoke(service, fRpcRequest.getParameters());
            log.info("service:{} invoke method:{}", fRpcRequest.getClassOrInterfaceName(), fRpcRequest.getMethodName());
        } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
