package com.fengxudong.frpc.remote.netty.codec;

import com.fengxudong.frpc.constant.FRpcConstant;
import com.fengxudong.frpc.enums.SerializationTypeEnum;
import com.fengxudong.frpc.remote.domain.FRpcMessage;
import com.fengxudong.frpc.remote.domain.FRpcRequest;
import com.fengxudong.frpc.remote.domain.FRpcResponse;
import com.fengxudong.frpc.serializer.Serializer;
import com.fengxudong.frpc.spi.FRpcLoader;
import com.fengxudong.frpc.spi.ServiceNameEnum;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @author feng xud
 */
@Slf4j
public class FrpcProtocolDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        log.info("FrpcProtocolDecoder decode ");
        /*
         * 丢弃长度 魔数和版本号，这里只是为了验证魔数和版本号是否正确，实际应用中可以不用这么做
         */
        in.skipBytes(FRpcConstant.LENGTH_FIELD_LENGTH);
        in.skipBytes(FRpcConstant.MAGIC_NUMBER_LENGTH);
        in.skipBytes(FRpcConstant.VERSION_LENGTH);

        // 读取请求头属性长度和属性值
        byte messageType = in.readByte();
        byte serializationType = in.readByte();
        FRpcMessage fRpcMessage = FRpcMessage.builder()
                .messageType(messageType)
                .serializationType(serializationType)
                .build();
        if(messageType==FRpcConstant.MessageType.HEARTBEAT_REQUEST_TYPE||messageType==FRpcConstant.MessageType.HEARTBEAT_RESPONSE_TYPE){
            out.add(fRpcMessage);
            return;
        }
        // 读取消息体长度和消息体
        int bodyLength = in.readInt();
        byte[] bodyBytes = new byte[bodyLength];
        in.readBytes(bodyBytes);
        Serializer serializer = FRpcLoader.getServiceLoader(Serializer.class).getTarget(ServiceNameEnum.SERIALIZER.getName());
        if(messageType==FRpcConstant.MessageType.REQUEST_TYPE){
            FRpcRequest request = serializer.deserialize(bodyBytes, FRpcRequest.class);
            log.info("FrpcProtocolDecoder decode FRpcRequest {}",request);
            fRpcMessage.setData(request);
        }
        if(messageType==FRpcConstant.MessageType.RESPONSE_TYPE){
            FRpcResponse fRpcResponse = serializer.deserialize(bodyBytes, FRpcResponse.class);
            fRpcMessage.setData(fRpcResponse);
            log.info("FrpcProtocolDecoder decode FRpcResponse {}",fRpcResponse);
        }
        // 将请求头属性和消息体封装成Message对象
        out.add(fRpcMessage);
    }
}
