1 activemq demo


2 kafka

启动本地集群：
/usr/local/Cellar/kafka/2.5.0/bin/kafka-server-start /usr/local/etc/kafka/server.properties &
/usr/local/Cellar/kafka/2.5.0/bin/kafka-server-start /usr/local/etc/kafka/server1.properties &
/usr/local/Cellar/kafka/2.5.0/bin/kafka-server-start /usr/local/etc/kafka/server2.properties &


创建一个有三个副本的topic
kafka-topics --create --zookeeper localhost:2181 --replication-factor 3 --partitions 1 --topic mytopic


使用describe命令查看topic的信息
kafka-topics --describe --zookeeper localhost:2181 --topic mytopic