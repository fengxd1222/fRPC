# 从零开始构建一个RPC

## RPC包含了什么？

> 开发一个组件或者一个系统，首先需要明确的是：
>
> - 想要实现的目标
> - 达到这个目标需要所需要步骤（可以宽泛一些，脉络清晰，细节慢慢填充）
>
> RPC的原理或者含义就不多说了，可以看看其他大神的博客。
>
> 实现一个RPC，或者实现跨进程通讯，那么最需要的是什么？
>
> 是通讯方式，无论是http或者是队列，其实都是一种应用层协议的体现，为了效率考虑，显然这二者都不合适，
>
> 可以使用`Netty`自实现一个通信框架，用过`Netty`的小伙伴都知道，使用`Netty`需要考虑如：序列化、拆包粘包、心跳等。

![image-20230506205645680](http://qair5z4qd.bkt.clouddn.com/image-20230506205645680.png)

> 上图是一个最简单的远程调用
>
> 说完通讯方式了，那么再思考一个问题，A应用如何知道B应用的IP和端口呢？总不能靠配置文件写死吧，如果更换IP了呢？如果是集群增加或减少了一个节点呢？那么此时就需要一个配置中心或者注册中心，ETCD或者Zookeeper都可以。



> 这是宏观上作为一个RPC框架需要的东西。