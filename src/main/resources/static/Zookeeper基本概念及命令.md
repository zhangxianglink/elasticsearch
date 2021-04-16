## zookeeper 基本概念

1. zookeeper 在Micro Service 中通常是作为注册中心来使用； 在中间件集群如kafka,起到元数据保存，

   节点监控，选举master的作用；在hadoop大数据环境，起到协调服务的作用。

   [故事](https://mp.weixin.qq.com/s/OKYlJsp_aEX3ObQ3Z5DpTQ)

> 客户端，服务端中间需要一个中间件，这个中间件要起到动态的服务注册与发现。
>
> 客户端只需要面向注册中心，中心提供可以使用的url , 采用树形结构，父节点代表服务，子节点代表
>
> 当前服务下可用的实例，注册中心和实例直接建立session回话，如果心跳停止，session过期，删除节点。
>
> 当作为分布式锁的时候， 多个系统谁先注册，谁持有锁，谁是master。为实现公平调度，多个系统注册
>
> 为子节点，按照编号获取资源，用完删除。

​      从上述故事可以得到单机zookeeper的核心概念

```sh
1.  Session ：表示某个客户系统（例如Batch Job）和ZooKeeper之间的连接会话,  Batch Job连上ZooKeeper以后会周期性地发送心跳信息， 如果Zookeeper在特定时间内收不到心跳，就会认为这个Batch Job已经死掉了, Session 就会结束。

2. 树形结构 :  树形结构中的每个节点叫做znode，父子节点是一对多形式，每个znode路径唯一，znode中数据可以
有多个版本。

3. znode类型 ：按类型可以分为永久的znode（除非主动删除，否则一直存在），持久顺序节点：父节点是第一层子节点，维护时序节点。临时的znode（Session结束就会删除）和 顺序znode（就是分布式锁中的process_01,process_02.....）。 

4.  Watch ：某个客户系统（例如Batch Job）可以监控znode， znode节点的变化（删除，修改数据等）都可以通知Batch Job， 这样Batch Job可以采取相应的动作，例如争抢着去创建节点。
```

## zookeeper基本命令

**查看节点**

```shell
[zk: localhost:2181(CONNECTED) 0] ls /
[cluster, controller_epoch, brokers, zookeeper, admin, isr_change_notification, consumers, log_dir_event_notification, latest_producer_id_block, config]
[zk: localhost:2181(CONNECTED) 1] ls /admin
[delete_topics]
[zk: localhost:2181(CONNECTED) 2] ls /admin/delete_topics
[]
```

**创建节点**

```shell
[zk: localhost:2181(CONNECTED) 8] create /test t1   
Created /test  # 默认持久节点
[zk: localhost:2181(CONNECTED) 9] create -s /test/sequence s1
Created /test/sequence0000000000  # 持久顺序节点
[zk: localhost:2181(CONNECTED) 10] create -s /test/sequence s2
Created /test/sequence0000000001  
[zk: localhost:2181(CONNECTED) 11] create -s /test/sequence s3
Created /test/sequence0000000002   # 临时节点 （不允许包含任何子节点）
[zk: localhost:2181(CONNECTED) 12] create -e /test/ephemeral_1 e1
Created /test/ephemeral_1
[zk: localhost:2181(CONNECTED) 13] create -e /test/ephemeral_2 e1
Created /test/ephemeral_2

[zk: localhost:2181(CONNECTED) 16] ls /test
[sequence0000000002, sequence0000000001, sequence0000000000, ephemeral_1, ephemeral_2]
[zk: localhost:2181(CONNECTED) 17] create -e -s /test/se se1        
Created /test/se0000000005         # 临时顺序节点 （同上）
[zk: localhost:2181(CONNECTED) 18] create -e -s /test/se se2
Created /test/se0000000006
```

**临时节点失效**

```shell
突然断开链接不会导致节点删除。
临时节点失效情况1：断开链接并且达到超时时间
~~~~~~~~~~~情况2：使用quit命令。
[zk: localhost:2181(CONNECTED) 20] ls /test
[sequence0000000002, se0000000006, sequence0000000001, sequence0000000000, ephemeral_2, ephemeral_1, se0000000005]
[zk: localhost:2181(CONNECTED) 21] quit
Quitting...
2021-04-16 13:49:46,231 [myid:] - INFO  [main:ZooKeeper@687] - Session: 0x1013ae190280002 closed
2021-04-16 13:49:46,233 [myid:] - INFO  [main-EventThread:ClientCnxn$EventThread@521] - EventThread shut down for session: 0x1013ae190280002
# 重新链接
[zk: localhost:2181(CONNECTED) 0] ls /test
[sequence0000000002, sequence0000000001, sequence0000000000]
```

**查看节点状态**

```shell
[zk: localhost:2181(CONNECTED) 6] stat /test                   
cZxid = 0x36
ctime = Fri Apr 16 13:37:31 CST 2021
mZxid = 0x36
mtime = Fri Apr 16 13:37:31 CST 2021
pZxid = 0x3e
cversion = 11
dataVersion = 0
aclVersion = 0
ephemeralOwner = 0x0
dataLength = 2
numChildren = 3
```

**RUD操作**

```shell
[zk: localhost:2181(CONNECTED) 7] get /test
t1
cZxid = 0x36
ctime = Fri Apr 16 13:37:31 CST 2021
mZxid = 0x36
mtime = Fri Apr 16 13:37:31 CST 2021
pZxid = 0x3e
cversion = 11
dataVersion = 0
aclVersion = 0
ephemeralOwner = 0x0
dataLength = 2
numChildren = 3
[zk: localhost:2181(CONNECTED) 8] set /test t----------
cZxid = 0x36
ctime = Fri Apr 16 13:37:31 CST 2021
mZxid = 0x42
mtime = Fri Apr 16 14:16:36 CST 2021
pZxid = 0x3e
cversion = 11
dataVersion = 1
aclVersion = 0
ephemeralOwner = 0x0
dataLength = 11
numChildren = 3
[zk: localhost:2181(CONNECTED) 9] get /test
t----------
cZxid = 0x36
ctime = Fri Apr 16 13:37:31 CST 2021
mZxid = 0x42
mtime = Fri Apr 16 14:16:36 CST 2021
pZxid = 0x3e
cversion = 11
dataVersion = 1
aclVersion = 0
ephemeralOwner = 0x0
dataLength = 11
numChildren = 3
[zk: localhost:2181(CONNECTED) 10] delete /test  
Node not empty: /test  # 当节点下有子节点，无法使用delete删除
[zk: localhost:2181(CONNECTED) 11] rmr /test  # 循环迭代删除
[zk: localhost:2181(CONNECTED) 12] ls /
[cluster, controller_epoch, brokers, zookeeper, admin, isr_change_notification, consumers, log_dir_event_notification, latest_producer_id_block, config]
```

**watch机制**

```shell
wather的特性：一次性，客户端串行执行，轻量。

一次性：对于ZK的watcher，你只需要记住一点：zookeeper有watch事件，是一次性触发的，当watch监视的数据发生变化时，通知设置了该watch的client，即watcher，由于zookeeper的监控都是一次性的，所以每次都必须监控。

客户端串行执行：客户端watcher回调的过程是一个串行同步的过程，这为我们保证了顺序，同时需要开发人员注意一点，千万不用因为一个watcher的处理逻辑影响了整个客户端的watcher回调。

轻量：WatcherEvent是Zookeeper整个Watcher通知机制的最小通知单元。整个单元结构只包含三部分：通知状态，事件类型和节点路径。也就是说Watcher通知非常的简单，只会告诉客户端发生了事件而不会告知其具体内容，需要客户自己去进行获取，而不会直接提供具体的数据内容。

 ls /node true 监听目录以及子目录的变化
 get /node true 监听当前目录数据变化
```

