
```
frpc-framework-core
├─ .gitignore
├─ .mvn
│  └─ wrapper
│     ├─ maven-wrapper.jar
│     └─ maven-wrapper.properties
├─ mvnw
├─ mvnw.cmd
├─ pom.xml
└─ src
   ├─ main
   │  ├─ java
   │  │  └─ com
   │  │     └─ fengxudong
   │  │        └─ frpc                                                
   │  │           ├─ annotation                          //核心注解                      
   │  │           │  ├─ FRpcReference.java               //引用服务                           
   │  │           │  ├─ FRpcScan.java                    //服务包路径扫描                     
   │  │           │  └─ FRpcService.java                 //服务提供                         
   │  │           ├─ config					             //内部配置类                    
   │  │           │  ├─ FRpcProxyConfig.java             //代理相关信息                            
   │  │           │  └─ FRpcServiceConfig.java           //service注解信息                        
   │  │           ├─ loadBalance                         //负载均衡                        
   │  │           │  ├─ AbstractLoadBalance.java         //抽象模板类                               
   │  │           │  ├─ LoadBalance.java                 //负载均衡接口                        
   │  │           │  ├─ RandomLoadBalancer.java	         //随机                                      
   │  │           │  └─ RoundRobinLoadBalancer.java      //轮询                                      
   │  │           ├─ provider                            //服务提供                    
   │  │           │  ├─ etcd                             //服务提供接口类                       
   │  │           │  ├─ ServiceProvider.java             //etcd（待实现）          
   │  │           │  └─ zk                               //zookeeper         
   │  │           │     ├─ ZkServiceProvider.java        //zk的服务提供实现类                      
   │  │           │     └─ ZkSupport.java                //zk客户端操作类                   
   │  │           ├─ proxy                               //代理相关                 
   │  │           │  ├─ cglib                            //代理相关的接口类                 
   │  │           │  │  └─ CglibProxy.java               //代理工厂                     
   │  │           │  ├─ FRpcProxy.java                   //cglib(待实现)      
   │  │           │  ├─ jdk                                                
   │  │           │  │  └─ JdkDynamicProxy.java          //jdk动态代理        
   │  │           │  └─ ProxyFactory.java                                                
   │  │           ├─ README.md						     //服务发现                   
   │  │           ├─ registry                            //服务发现接口类                     
   │  │           │  ├─ etcd                             //etcd（待实现）       
   │  │           │  ├─ ServiceDiscovery.java            //zk             
   │  │           │  └─ zk                                                
   │  │           │     └─ ZkServiceDiscovery.java       //远程通信                
   │  │           ├─ remote                              //远程通信接口                    
   │  │           │  ├─ domain                           //远程通信的实体类          
   │  │           │  │  ├─ FRpcMessage.java              //消息体                     
   │  │           │  │  ├─ FRpcRequest.java
   │  │           │  │  └─ FRpcResponse.java
   │  │           │  ├─ FRpcTransport.java
   │  │           │  └─ netty
   │  │           │     ├─ client
   │  │           │     │  ├─ ChannelHolder.java
   │  │           │     │  ├─ EventLoopGroupBuilder.java
   │  │           │     │  ├─ FRpcNettyTransport.java
   │  │           │     │  ├─ handler
   │  │           │     │  │  └─ ClientHandler.java
   │  │           │     │  └─ ResponseFutureHolder.java
   │  │           │     ├─ codec
   │  │           │     │  ├─ FRpcLengthFieldBasedFrameDecoder.java
   │  │           │     │  ├─ FrpcProtocolDecoder.java
   │  │           │     │  └─ FRpcProtocolEncoder.java
   │  │           │     ├─ FRpcInitializer.java
   │  │           │     └─ server
   │  │           │        ├─ EventLoopGroupBuilder.java
   │  │           │        ├─ FRpcNettyServer.java
   │  │           │        └─ handler
   │  │           │           └─ ServerHandler.java
   │  │           ├─ scan
   │  │           │  ├─ FRpcScanner.java
   │  │           │  ├─ FRpcScannerRegistrar.java
   │  │           │  ├─ FRpcServiceBeanPostProcessor.java
   │  │           │  └─ util
   │  │           ├─ serializer
   │  │           │  ├─ kryo
   │  │           │  │  └─ KryoSerializer.java
   │  │           │  └─ Serializer.java
   │  │           └─ spi
   │  │              ├─ FRpcLoader.java
   │  │              ├─ FRpcSPI.java
   │  │              └─ ServiceNameEnum.java
   │  └─ resources
   │     └─ META-INF
   │        └─ services
   │           ├─ com.fengxudong.frpc.loadBalance.LoadBalance
   │           ├─ com.fengxudong.frpc.provider.ServiceProvider
   │           ├─ com.fengxudong.frpc.registry.ServiceDiscovery
   │           ├─ com.fengxudong.frpc.remote.FRpcTransport
   │           └─ com.fengxudong.frpc.serializer.Serializer
   └─ test
      └─ java
         └─ com
            └─ fengxudong
               └─ frpc
                  └─ FrpcFrameworkCoreApplicationTests.java

```