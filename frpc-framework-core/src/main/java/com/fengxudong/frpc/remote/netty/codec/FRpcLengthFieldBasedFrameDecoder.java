package com.fengxudong.frpc.remote.netty.codec;

import com.fengxudong.frpc.constant.FRpcConstant;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author feng xud
 */
@Slf4j
public class FRpcLengthFieldBasedFrameDecoder extends LengthFieldBasedFrameDecoder {
    private final int maxFrameLength;
    private final int lengthFieldOffset;
    private final int lengthFieldLength;
    public FRpcLengthFieldBasedFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
        this.maxFrameLength = maxFrameLength;
        this.lengthFieldOffset = lengthFieldOffset;
        this.lengthFieldLength = lengthFieldLength;
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        log.info("FRpcLengthFieldBasedFrameDecoder decode ");
        //读取消息长度
        byte[] bytes = new byte[FRpcConstant.LENGTH_FIELD_LENGTH];
        in.getBytes(0,bytes);
        // 验证魔数和版本号是否正确
        if (!checkMagicNumber(in) || !checkVersion(in)) {
            throw new Exception("Invalid protocol header");
        }
        // 重置读指针，读取整个消息
        in.resetReaderIndex();
        return super.decode(ctx, in);
    }

    private boolean checkMagicNumber(ByteBuf buf) {
        byte[] magicNumber = new byte[FRpcConstant.MAGIC_NUMBER_LENGTH];
        buf.getBytes(FRpcConstant.LENGTH_FIELD_LENGTH, magicNumber);
        return java.util.Arrays.equals(magicNumber, FRpcConstant.MAGIC_NUMBER);
    }

    private boolean checkVersion(ByteBuf buf) {
        byte[] version = new byte[FRpcConstant.VERSION_LENGTH];
        buf.getBytes(FRpcConstant.MAGIC_NUMBER_LENGTH+FRpcConstant.LENGTH_FIELD_LENGTH, version);
        return java.util.Arrays.equals(version, FRpcConstant.VERSION);
    }
}
