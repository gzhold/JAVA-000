1 按自己设计的表结构，插入100万订单模拟数据，测试不同方式的插入效率
工程: batch-save-data

做了两种方式批量插入，一个批次数据量也不易过多，mysql数据包限制


2 动态数据源
工程：dynamic-datasource

1.1 基于操作AbstractRoutingDataSource
1.2 支持配置多个从库;
1.3 支持多个从库的负载均衡


3 读写分离
工程：shardingSphere-jdbc-master-slave