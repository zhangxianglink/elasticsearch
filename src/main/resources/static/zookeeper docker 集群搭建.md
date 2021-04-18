## zookeeper docker 集群搭建

```shell
# 先启动三个镜像
[root@iZuf64kurfu1ylla91wcwtZ ~]# docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' myzk
172.17.0.3
[root@iZuf64kurfu1ylla91wcwtZ ~]# docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' myzk2
172.17.0.2
[root@iZuf64kurfu1ylla91wcwtZ ~]# docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' myzk3
172.17.0.4
```

**集群搭建**

```shell
# 进入容器
docker exec -it myzk /bin/bash
# 安装vim
apt-get update
apt-get install vim
vim /conf/zoo.cfg
# 删掉server.1=localhost:2888:3888;2181, 添加下面内容
4lw.commands.whitelist=*
clientPort=2181
server.1=172.17.0.2:2888:3888
server.2=172.17.0.3:2888:3888
server.3=172.17.0.4:2888:3888

# 根据上述server的映射关系，修改myid
sed -i '1c 2' /data/myid 

# 编辑完成，重启容器 （其他集群亦然）
docker restart 「container」


```

**搭建✅查看**

```shell
模拟top命令查看状态：
echo stats | nc 172.17.0.4 2181
echo stats | nc 172.17.0.2 2181
echo stats | nc 172.17.0.3 2181
```

