# fRPC
A custom implementation of an RPC project

## frpc-framework-core目录结构

````
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
   │  │           ├─ config                              //内部配置类                    
   │  │           │  ├─ FRpcProxyConfig.java             //代理相关信息                            
   │  │           │  └─ FRpcServiceConfig.java           //service注解信息                        
   │  │           ├─ loadBalance                         //负载均衡                        
   │  │           │  ├─ AbstractLoadBalance.java         //抽象模板类                               
   │  │           │  ├─ LoadBalance.java                 //负载均衡接口                        
   │  │           │  ├─ RandomLoadBalancer.java          //随机                                      
   │  │           │  └─ RoundRobinLoadBalancer.java      //轮询                                      
   │  │           ├─ provider                            //服务提供                    
   │  │           │  ├─ etcd                             //服务提供接口类                       
   │  │           │  ├─ ServiceProvider.java             //etcd（待实现）          
   │  │           │  └─ zk                               //zookeeper         
   │  │           │     ├─ ZkServiceProvider.java        //zk的服务提供实现类                      
   │  │           │     └─ ZkSupport.java                //zk客户端操作类                   
   │  │           ├─ proxy                               //代理相关                 
   │  │           │  ├─ cglib                            //cglib(待实现)                
   │  │           │  │  └─ CglibProxy.java               //代理工厂                     
   │  │           │  ├─ FRpcProxy.java                   //代理相关的接口类       
   │  │           │  ├─ jdk                                                
   │  │           │  │  └─ JdkDynamicProxy.java          //jdk动态代理        
   │  │           │  └─ ProxyFactory.java                //代理工厂                                 
   │  │           ├─ README.md                                              
   │  │           ├─ registry                            //服务发现                
   │  │           │  ├─ etcd                             //etcd（待实现）       
   │  │           │  ├─ ServiceDiscovery.java            //服务发现接口类            
   │  │           │  └─ zk                               //zk                  
   │  │           │     └─ ZkServiceDiscovery.java                           
   │  │           ├─ remote                              //远程通信                    
   │  │           │  ├─ domain                           //远程通信的实体类          
   │  │           │  │  ├─ FRpcMessage.java              //消息体                     
   │  │           │  │  ├─ FRpcRequest.java              //请求
   │  │           │  │  └─ FRpcResponse.java             //响应
   │  │           │  ├─ FRpcTransport.java               //远程通信接口
   │  │           │  └─ netty                            //基于netty实现的通信
   │  │           │     ├─ client                        //客户端相关
   │  │           │     │  ├─ ChannelHolder.java         //channel持有类
   │  │           │     │  ├─ EventLoopGroupBuilder.java
   │  │           │     │  ├─ FRpcNettyTransport.java    //netty客户端
   │  │           │     │  ├─ handler                    //客户端相关handler
   │  │           │     │  │  └─ ClientHandler.java
   │  │           │     │  └─ ResponseFutureHolder.java  //响应future持有类
   │  │           │     ├─ codec                         //加解码相关   
   │  │           │     │  ├─ FRpcLengthFieldBasedFrameDecoder.java
   │  │           │     │  ├─ FrpcProtocolDecoder.java
   │  │           │     │  └─ FRpcProtocolEncoder.java
   │  │           │     ├─ FRpcInitializer.java
   │  │           │     └─ server                        //服务端相关
   │  │           │        ├─ EventLoopGroupBuilder.java
   │  │           │        ├─ FRpcNettyServer.java       //netty服务端
   │  │           │        └─ handler                    //服务端相关handler
   │  │           │           └─ ServerHandler.java
   │  │           ├─ scan                                //FRpc相关注解扫描
   │  │           │  ├─ FRpcScanner.java
   │  │           │  ├─ FRpcScannerRegistrar.java
   │  │           │  ├─ FRpcServiceBeanPostProcessor.java
   │  │           │  └─ util
   │  │           ├─ serializer                          //序列化相关
   │  │           │  ├─ kryo                             //kryo协议
   │  │           │  │  └─ KryoSerializer.java
   │  │           │  └─ Serializer.java
   │  │           └─ spi                                 //spi注入相关
   │  │              ├─ FRpcLoader.java
   │  │              ├─ FRpcSPI.java
   │  │              └─ ServiceNameEnum.java
   │  └─ resources
   │     └─ META-INF
   │        └─ services
   │           ├─ com.fengxudong.frpc.loadBalance.LoadBalance   //负载均衡spi
   │           ├─ com.fengxudong.frpc.provider.ServiceProvider  //服务提供spi
   │           ├─ com.fengxudong.frpc.registry.ServiceDiscovery //服务发现spi
   │           ├─ com.fengxudong.frpc.remote.FRpcTransport      //通信方式spi
   │           └─ com.fengxudong.frpc.serializer.Serializer     //序列化方式spi
   └─ test
      └─ java
         └─ com
            └─ fengxudong
               └─ frpc
                  └─ FrpcFrameworkCoreApplicationTests.java

```
````
