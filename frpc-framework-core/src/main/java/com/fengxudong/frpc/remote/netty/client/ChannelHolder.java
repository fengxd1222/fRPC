package com.fengxudong.frpc.remote.netty.client;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author feng xud
 */
public class ChannelHolder {

    private final Map<String, Channel> channelMap;

    public ChannelHolder() {
        this.channelMap = new ConcurrentHashMap<>();
    }

    public Channel get(InetSocketAddress inetSocketAddress){
        String channelKey = inetSocketAddress.toString();
        if(channelMap.containsKey(channelKey)){
            Channel channel = channelMap.get(channelKey);
            if(channel!=null && !channel.isActive()){
                return channel;
            }
        }
        return null;
    }

    public void set(InetSocketAddress inetSocketAddress,Channel channel){
        String channelKey = inetSocketAddress.toString();
        channelMap.put(channelKey,channel);
    }
}
