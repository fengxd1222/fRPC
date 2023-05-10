package com.fengxudong.frpc.remote.netty;

import com.fengxudong.frpc.constant.FRpcConstant;
import com.fengxudong.frpc.remote.netty.client.handler.ClientHandler;
import com.fengxudong.frpc.remote.netty.codec.FRpcLengthFieldBasedFrameDecoder;
import com.fengxudong.frpc.remote.netty.codec.FRpcProtocolEncoder;
import com.fengxudong.frpc.remote.netty.codec.FrpcProtocolDecoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author feng xud
 */
public class FRpcInitializer extends ChannelInitializer {
    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        //心跳檢測
        pipeline.addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
        // 添加长度加码器和自定义协议编码器
        pipeline.addLast(new LengthFieldPrepender(FRpcConstant.LENGTH_FIELD_LENGTH));
        pipeline.addLast(new FRpcProtocolEncoder());

        // 添加长度解码器和自定义协议解码器
        pipeline.addLast(new FRpcLengthFieldBasedFrameDecoder(
                Integer.MAX_VALUE,
                FRpcConstant.LENGTH_FIELD_OFFSET,
                FRpcConstant.LENGTH_FIELD_LENGTH));
        pipeline.addLast(new FrpcProtocolDecoder());

        // todo 添加自定义逻辑处理器
        pipeline.addLast(new ClientHandler());
    }
}
