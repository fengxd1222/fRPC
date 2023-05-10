│    │    │                  └─ frpc
│    │    │                         ├─ README.md
│    │    │                         ├─ annotation                                                                //核心注解
│    │    │                         │    ├─ FRpcReference.java                                              //引用服务
│    │    │                         │    ├─ FRpcScan.java                                                      //服务包路径扫描
│    │    │                         │    └─ FRpcService.java                                                  //服务提供
│    │    │                         ├─ config 																	  //内部配置类
│    │    │                         │    ├─ FRpcProxyConfig.java                                         //代理相关信息
│    │    │                         │    └─ FRpcServiceConfig.java                                      //service注解信息
│    │    │                         ├─ loadBalance                                                             //负载均衡
│    │    │                         │    ├─ AbstractLoadBalance.java                                   //抽象模板类
│    │    │                         │    ├─ LoadBalance.java                                                 //负载均衡接口
│    │    │                         │    ├─ RandomLoadBalancer.java								   //随机
│    │    │                         │    └─ RoundRobinLoadBalancer.java                            //轮询
│    │    │                         ├─ provider                                                                    //服务提供
│    │    │                         │    ├─ ServiceProvider.java                                             //服务提供接口类
│    │    │                         │    ├─ etcd                                                                        //etcd（待实现）
│    │    │                         │    └─ zk                                                                           //zookeeper
│    │    │                         │           ├─ ZkServiceProvider.java                                  //zk的服务提供实现类
│    │    │                         │           └─ ZkSupport.java                                               //zk客户端操作类
│    │    │                         ├─ proxy                                                                          //代理相关
│    │    │                         │    ├─ FRpcProxy.java                                                        //代理相关的接口类
│    │    │                         │    ├─ ProxyFactory.java                                                     //代理工厂
│    │    │                         │    ├─ cglib                                                                          //cglib(待实现)
│    │    │                         │    │    └─ CglibProxy.java
│    │    │                         │    └─ jdk                                                                             //jdk动态代理
│    │    │                         │           └─ JdkDynamicProxy.java
│    │    │                         ├─ registry																		//服务发现
│    │    │                         │    ├─ ServiceDiscovery.java                                              //服务发现接口类
│    │    │                         │    ├─ etcd                                                                           //etcd（待实现）
│    │    │                         │    └─ zk                                                                              //zk
│    │    │                         │           └─ ZkServiceDiscovery.java
│    │    │                         ├─ remote                                                                          //远程通信
│    │    │                         │    ├─ FRpcTransport.java                                                   //远程通信接口
│    │    │                         │    ├─ domain                                                                       //远程通信的实体类
│    │    │                         │    │    ├─ FRpcMessage.java                                                  //消息体
│    │    │                         │    │    ├─ FRpcRequest.java						                           //请求
│    │    │                         │    │    └─ FRpcResponse.java                                                 //响应
│    │    │                         │    └─ netty                                                                            //基于netty实现的通信
│    │    │                         │           ├─ FRpcInitializer.java                                                    //client端handler配置
│    │    │                         │           ├─ client                                                                           //客户端相关
│    │    │                         │           │    ├─ ChannelHolder.java                                                    //channel连接持有者
│    │    │                         │           │    ├─ EventLoopGroupBuilder.java
│    │    │                         │           │    ├─ FRpcNettyTransport.java                                            //client端
│    │    │                         │           │    ├─ ResponseFutureHolder.java                                        //响应持有者