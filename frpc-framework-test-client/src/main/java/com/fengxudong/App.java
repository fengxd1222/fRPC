package com.fengxudong;

import com.fengxudong.client.TestFrpcService;
import com.fengxudong.frpc.annotation.FRpcScan;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 *
 */
@FRpcScan(basePackages = {"com.fengxudong"})
public class App 
{
    public static void main( String[] args ) throws InterruptedException {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(App.class);
        TestFrpcService bean = applicationContext.getBean(TestFrpcService.class);
        bean.sout();

        Thread.sleep(30 * 1000);
    }
}
