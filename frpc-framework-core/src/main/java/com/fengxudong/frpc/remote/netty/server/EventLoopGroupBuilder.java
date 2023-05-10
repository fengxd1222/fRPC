package com.fengxudong.frpc.remote.netty.server;

import com.fengxudong.frpc.constant.FRpcConstant;
import com.fengxudong.frpc.remote.netty.codec.FRpcLengthFieldBasedFrameDecoder;
import com.fengxudong.frpc.remote.netty.codec.FRpcProtocolEncoder;
import com.fengxudong.frpc.remote.netty.codec.FrpcProtocolDecoder;
import com.fengxudong.frpc.remote.netty.server.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.MultithreadEventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;


public class EventLoopGroupBuilder {

    static String os;

    static {
        os = System.getProperty("os.name");
    }

    public Group<MultithreadEventLoopGroup, MultithreadEventLoopGroup> build() {
        //Linux操作系统 使用epoll
        if (os != null && os.toLowerCase().startsWith("LINUX")) {
            return new Group<>(new EpollEventLoopGroup(1), new EpollEventLoopGroup(10),true);
        } else {
            return new Group<>(new NioEventLoopGroup(1), new NioEventLoopGroup(10),false);
        }
    }

    public static ChannelHandler[] channelHandlers(){
        return new ChannelHandler[]{
                new LoggingHandler(LogLevel.INFO)
        };
    }

    public static ChannelHandler[] channelChildHandlers(){
        return new ChannelHandler[]{
                new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS),
                new LengthFieldPrepender(FRpcConstant.LENGTH_FIELD_LENGTH),
                new FRpcProtocolEncoder(),
                new FRpcLengthFieldBasedFrameDecoder(
                        Integer.MAX_VALUE,
                        FRpcConstant.LENGTH_FIELD_OFFSET,
                        FRpcConstant.LENGTH_FIELD_LENGTH),
                new FrpcProtocolDecoder(),
                new ServerHandler()};
    }

    public static void addHandler(ServerBootstrap serverBootstrap){
        if (os != null && os.toLowerCase().startsWith("linux")) {
            serverBootstrap.handler(new ChannelInitializer<EpollServerSocketChannel>(){
                @Override
                protected void initChannel(EpollServerSocketChannel ch) throws Exception {
                    ch.pipeline().addLast(channelHandlers());
                }
            });
        }else {
            serverBootstrap.handler(new ChannelInitializer<NioServerSocketChannel>(){
                @Override
                protected void initChannel(NioServerSocketChannel ch) throws Exception {
                    ch.pipeline().addLast(channelHandlers());
                }
            });
        }
    }
    public static void addChildHandler(ServerBootstrap serverBootstrap){
        if (os != null && os.toLowerCase().startsWith("linux")) {
            serverBootstrap.childHandler(new ChannelInitializer<EpollSocketChannel>(){
                @Override
                protected void initChannel(EpollSocketChannel ch) throws Exception {
                    ch.pipeline().addLast(channelChildHandlers());
                }
            });
        }else {
            serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>(){
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ch.pipeline().addLast(channelChildHandlers());
                }
            });
        }
    }


    public class Group<P, C> {
        private MultithreadEventLoopGroup parentEventLoopGroup;
        private MultithreadEventLoopGroup childEventLoopGroup;

        private Class<? extends ServerSocketChannel> channelClass;

        public Group(MultithreadEventLoopGroup p, MultithreadEventLoopGroup c,boolean isLinux) {
            parentEventLoopGroup = p;
            childEventLoopGroup = c;
            if(isLinux){
                this.channelClass = EpollServerSocketChannel.class;
            }else {
                this.channelClass = NioServerSocketChannel.class;
            }
        }

        public MultithreadEventLoopGroup getP() {
            return parentEventLoopGroup;
        }

        public MultithreadEventLoopGroup getC() {
            return childEventLoopGroup;
        }

        public Class<? extends ServerSocketChannel> getChannelClass() {
            return channelClass;
        }
    }
}
