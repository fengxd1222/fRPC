package com.fengxudong.frpc.remote.netty.codec;

import com.fengxudong.frpc.constant.FRpcConstant;
import com.fengxudong.frpc.enums.SerializationTypeEnum;
import com.fengxudong.frpc.remote.domain.FRpcMessage;
import com.fengxudong.frpc.serializer.Serializer;
import com.fengxudong.frpc.spi.FRpcLoader;
import com.fengxudong.frpc.spi.ServiceNameEnum;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;


/**
 * @author feng xud
 */
@Slf4j
public class FRpcProtocolEncoder extends MessageToByteEncoder<FRpcMessage> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, FRpcMessage fRpcMessage, ByteBuf out) throws Exception {
        log.info("FRpcProtocolEncoder encode FRpcMessage {}",fRpcMessage);
        // 写入魔数和版本号
        out.writeBytes(FRpcConstant.MAGIC_NUMBER);
        out.writeBytes(FRpcConstant.VERSION);

        // 写入请求头属性长度和属性值
        byte messageType = fRpcMessage.getMessageType();
        out.writeByte(messageType);
        out.writeByte(fRpcMessage.getSerializationType());

        // 写入消息体长度和消息体
        byte[] bytes = null;
        if(messageType==FRpcConstant.MessageType.REQUEST_TYPE||messageType==FRpcConstant.MessageType.RESPONSE_TYPE){
            bytes = FRpcLoader.getServiceLoader(Serializer.class).getTarget(ServiceNameEnum.SERIALIZER.getName()).serialize(fRpcMessage.getData());
            out.writeInt(bytes.length);
            out.writeBytes(bytes);
        }
    }
}
