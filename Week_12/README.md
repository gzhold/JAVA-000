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

启动主从实例
./redis-server /usr/local/Cellar/redis/sentinel/63791.conf
./redis-server /usr/local/Cellar/redis/sentinel/63792.conf
./redis-server /usr/local/Cellar/redis/sentinel/63793.conf

默认 63791是主，其他两个是从



启动哨兵
./redis-sentinel /usr/local/Cellar/redis/sentinel/sentinel.conf


手动关闭63791，哨兵切换，63792为主节点


**哨兵切换日志**
78033:X 04 Jan 2021 22:17:59.569 # +sdown master mymaster 127.0.0.1 63791
78033:X 04 Jan 2021 22:17:59.569 # +odown master mymaster 127.0.0.1 63791 #quorum 1/1
78033:X 04 Jan 2021 22:17:59.570 # +new-epoch 1
78033:X 04 Jan 2021 22:17:59.570 # +try-failover master mymaster 127.0.0.1 63791
78033:X 04 Jan 2021 22:17:59.571 # +vote-for-leader 54689a03426ff221f1d985ab46968c06f07478fc 1
78033:X 04 Jan 2021 22:17:59.572 # +elected-leader master mymaster 127.0.0.1 63791
78033:X 04 Jan 2021 22:17:59.572 # +failover-state-select-slave master mymaster 127.0.0.1 63791
78033:X 04 Jan 2021 22:17:59.631 # +selected-slave slave 127.0.0.1:63792 127.0.0.1 63792 @ mymaster 127.0.0.1 63791
78033:X 04 Jan 2021 22:17:59.631 * +failover-state-send-slaveof-noone slave 127.0.0.1:63792 127.0.0.1 63792 @ mymaster 127.0.0.1 63791
78033:X 04 Jan 2021 22:17:59.688 * +failover-state-wait-promotion slave 127.0.0.1:63792 127.0.0.1 63792 @ mymaster 127.0.0.1 63791
78033:X 04 Jan 2021 22:18:00.534 # +promoted-slave slave 127.0.0.1:63792 127.0.0.1 63792 @ mymaster 127.0.0.1 63791
78033:X 04 Jan 2021 22:18:00.534 # +failover-state-reconf-slaves master mymaster 127.0.0.1 63791
78033:X 04 Jan 2021 22:18:00.590 * +slave-reconf-sent slave 127.0.0.1:63793 127.0.0.1 63793 @ mymaster 127.0.0.1 63791
78033:X 04 Jan 2021 22:18:01.592 * +slave-reconf-inprog slave 127.0.0.1:63793 127.0.0.1 63793 @ mymaster 127.0.0.1 63791
78033:X 04 Jan 2021 22:18:01.592 * +slave-reconf-done slave 127.0.0.1:63793 127.0.0.1 63793 @ mymaster 127.0.0.1 63791
78033:X 04 Jan 2021 22:18:01.673 # +failover-end master mymaster 127.0.0.1 63791
78033:X 04 Jan 2021 22:18:01.673 # +switch-master mymaster 127.0.0.1 63791 127.0.0.1 63792
78033:X 04 Jan 2021 22:18:01.673 * +slave slave 127.0.0.1:63793 127.0.0.1 63793 @ mymaster 127.0.0.1 63792
78033:X 04 Jan 2021 22:18:01.674 * +slave slave 127.0.0.1:63791 127.0.0.1 63791 @ mymaster 127.0.0.1 63792
78033:X 04 Jan 2021 22:18:11.682 # +sdown slave 127.0.0.1:63791 127.0.0.1 63791 @ mymaster 127.0.0.1 63792


**63792 日志**
77913:S 04 Jan 2021 22:17:59.699 # Error condition on socket for SYNC: Operation now in progress
77913:S 04 Jan 2021 22:18:00.591 * REPLICAOF 127.0.0.1:63792 enabled (user request from 'id=9 addr=127.0.0.1:58075 fd=9 name=sentinel-54689a03-cmd age=31 idle=0 flags=x db=0 sub=0 psub=0 multi=4 qbuf=197 qbuf-free=32571 obl=45 oll=0 omem=0 events=r cmd=exec user=default')
77913:S 04 Jan 2021 22:18:00.593 # CONFIG REWRITE executed with success.
77913:S 04 Jan 2021 22:18:00.733 * Connecting to MASTER 127.0.0.1:63792
77913:S 04 Jan 2021 22:18:00.733 * MASTER <-> REPLICA sync started
77913:S 04 Jan 2021 22:18:00.734 * Non blocking connect for SYNC fired the event.
77913:S 04 Jan 2021 22:18:00.734 * Master replied to PING, replication can continue...
77913:S 04 Jan 2021 22:18:00.734 * Trying a partial resynchronization (request fa175885da36016b93aebc78ddd48b269e61b62c:2012).
77913:S 04 Jan 2021 22:18:00.735 * Successful partial resynchronization with master.
77913:S 04 Jan 2021 22:18:00.735 # Master replication ID changed to 6c5bbb2ecd74a221d96aa223265d068546aeb9e4
77913:S 04 Jan 2021 22:18:00.735 * MASTER <-> REPLICA sync: Master accepted a Partial Resynchronization.

**63793日志**
77915:S 04 Jan 2021 22:17:59.186 # Error condition on socket for SYNC: Operation now in progress
77915:M 04 Jan 2021 22:17:59.688 * Discarding previously cached master state.
77915:M 04 Jan 2021 22:17:59.688 # Setting secondary replication ID to fa175885da36016b93aebc78ddd48b269e61b62c, valid up to offset: 2012. New replication ID is 6c5bbb2ecd74a221d96aa223265d068546aeb9e4
77915:M 04 Jan 2021 22:17:59.688 * MASTER MODE enabled (user request from 'id=8 addr=127.0.0.1:58073 fd=9 name=sentinel-54689a03-cmd age=30 idle=0 flags=x db=0 sub=0 psub=0 multi=4 qbuf=188 qbuf-free=32580 obl=45 oll=0 omem=0 events=r cmd=exec user=default')
77915:M 04 Jan 2021 22:17:59.691 # CONFIG REWRITE executed with success.
77915:M 04 Jan 2021 22:18:00.735 * Replica 127.0.0.1:63793 asks for synchronization
77915:M 04 Jan 2021 22:18:00.735 * Partial resynchronization request from 127.0.0.1:63793 accepted. Sending 157 bytes of backlog starting from offset 2012.