# yim
基于Netty，ZooKeeper和Redis的分布式IM。持续学习持续更新...

-  `客户端`与`服务端`的连接，通过`路由端`选择可用`服务端`节点
-  `服务端`启动后，向`ZooKeeper`注册
-  `客户端`发送消息通过`路由端`，选择对应的`服务端`进行推送消息
 
## TODO LIST
* [x] 客户端服务端连接
* [x] 客户端处理用户输入消息及系统指令
* [x] Google Protocol 消息序列化
* [x] 服务端向路由注册
* [x] 路由端获取可用服务端节点
* [x] 消息群发功能
* [x] 消息私发功能
* [x] 客户端退出系统
* [x] 客户端下线(可以使用系统命令直接重新登录)
* [x] 服务端断线，客户端重连
* [ ] 心跳
* [ ] 通讯信息存储
* [ ] 客户端离线消息

## 运行
1.启动Redis与Zookeeper

2.启动路由
```
yim-route
```	
3.启动服务端
```
yim-server(本地下打jar包运行指定port，或者直接先启动一个然后改一下端口号再启动)
```	
4.启动客户端
```
yim-client(本地下打jar包运行指定port,userId,username，或者直接先启动一个然后改一下端口号以及userId和username再启动)
```	
参考资料：https://crossoverjie.top/2019/01/02/netty/cim01-started/
