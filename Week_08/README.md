1 基于 shardingsphere-5.0.0-proxy官方的examples 实现

(1) 本地已创建好了两个库kad 和 test，分别在两个库 创建t_order_1 和  t_order_2,作业是要求16个表，原理是一样的，分别建2个表测试

CREATE TABLE `t_order_1` (
  `order_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `status` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=279205305122816001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


CREATE TABLE `t_order_2` (
  `order_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `status` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=279205305122816001 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

(2) 修改server.xml 和 config-sharding.yaml，启动shardingsphere-proxy

脚本: bin/start.sh  （需要将mysql-connector的jar 复制到 lib/ 目录）

登录proxy的mysql库，默认3307，数据库配置：sharding_db

因为配置的分片算法是
ds_${user_id % 2}  用user_id 取2的模，计算数据库
t_order_${order_id % 2}  order_id 取2的模，计算表


分别观察以下语句，数据存储是否和预期一样。
select * from t_order;

insert into t_order(user_id, status) values(123, "1")

insert into t_order(user_id, status) values(2, "2")

insert into t_order(user_id, status) values(1, "1")

insert into t_order(user_id, status) values(3, "2")


select * from t_order where user_id = 3;

（3）打开proxy的配置： 打印sql为true
可以观察真实sql