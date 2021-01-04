1.（必做）配置 redis 的主从复制，sentinel 高可用，Cluster 集群

mac单机

(1) 搭建主从， 默认 master 6379， slave 6380
启动主从
redis-server /usr/local/etc/redis-master-slave/master.conf
redis-server /usr/local/etc/redis-master-slave/slave.conf

telnet master 6379
输入info

# Replication
role:master
connected_slaves:1
slave0:ip=127.0.0.1,port=6380,state=online,offset=364,lag=1
master_replid:9fe2bf7a240c92b84577cbbef7ef86e18ed9c162
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:364
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:364



telnet master 6380
输入info

# Replication
role:slave
master_host:127.0.0.1
master_port:6379
...

# Keyspace
db0:keys=18,expires=0,avg_ttl=0

在从库 查询指定key
get photo_obj_id
$10
3301000051


（2）哨兵
启动哨兵
redis-sentinel /usr/local/etc/redis-sentinel.conf --sentinel

