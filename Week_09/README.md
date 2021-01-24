学习笔记

1、（选做）实现简单的Protocol Buffer/Thrift/gRPC(选任一个)远程调用demo。

### gRPC

1. 使用 gRPC maven 插件按照 protobuf 源文件生成 gRPC 相关的 Java 文件。
2. 将生成好的文件放到对应的类路径下，完成 client 端和 sever 端代码

代码地址[rpc-grpc](https://github.com/oliverschen/JAVA-000/tree/main/Week_09/rpc-grpc)

### RPC

RPC：远程过程调用，简单来说就是“像调用本地方法一样调用远程方法”

#### 原理

**核心是代理机制。**

1.本地代理存根: Stub

2.本地序列化反序列化

3.网络通信

4.远程序列化反序列化

5.远程服务存根: Skeleton

6.调用实际业务服务

7.原路返回服务结果

8.返回给本地调用方



3、（必做）改造自定义RPC的程序，提交到github： 

​	1）尝试将服务端写死查找接口实现类变成泛型和反射

将需要 rpc 调用的类添加 @Service 注解，交给 Spring 管理，在 Spring BeanPostProcessor 后置方法中放入一个 map 中，以Map<接口名，实现类> 的方式。

在 provider 中调用时直接用接口名调用，consumer 在 Map 中就可以获取到对应的具体实现类。

​	2）尝试将客户端动态代理改成AOP，添加异常处理

用 bytebuddy 方法拦截的形式实现代理，bytebuddy API 使用起来很友好，很容易上手。

​	3）尝试使用Netty+HTTP作为client端传输方式

用 netty 发送 http 请求实现

代码地址[rpcfx](https://github.com/oliverschen/JAVA-000/tree/main/Week_09/rpcfx)

4、（选做☆☆）升级自定义RPC的程序：（待实现）

​	1）尝试使用压测并分析优化RPC性能

​	2）尝试使用Netty+TCP作为两端传输方式

​	3）尝试自定义二进制序列化

​	4）尝试压测改进后的RPC并分析优化，有问题欢迎群里讨论

​	5）尝试将fastjson改成xstream

​	6）尝试使用字节码生成方式代替服务端反射