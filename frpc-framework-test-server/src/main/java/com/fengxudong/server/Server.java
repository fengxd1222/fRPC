package com.fengxudong.server;

import com.fengxudong.frpc.annotation.FRpcScan;
import com.fengxudong.frpc.remote.netty.server.FRpcNettyServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author feng xud
 */
@FRpcScan(basePackages = {"com.fengxudong"})
public class Server {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Server.class);
        new FRpcNettyServer().run();
    }
}
