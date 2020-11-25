1[必做]基于电商交易场景(用户、商品、订单)，设计一套简单的表结构
参考了网上的表设计：

订单表：
CREATE TABLE `t_order` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `order_no` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单编号',
  `order_transaction_no` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '交易号',
  `member_id` int(11) NOT NULL COMMENT '客户编号',
  `supplier_id` int(11) NOT NULL COMMENT '商户编码',
  `supplier_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '商户名称',
  `order_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '订单状态 0未付款,1已付款,2已发货,3已签收,-1退货申请,-2退货中,-3已退货,-4取消交易',
  `after_sales_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '用户售后状态 0 未发起售后 1 申请售后 -1 售后已取消 2 处理中 200 处理完毕',
  `product_count` int(11) NOT NULL DEFAULT '0' COMMENT '商品数量',
  `product_amount_total` decimal(12,4) NOT NULL COMMENT '商品总价',
  `order_amount_total` decimal(12,4) NOT NULL DEFAULT '0.0000' COMMENT '实际付款金额',
  `logistics_fee` decimal(12,4) NOT NULL COMMENT '运费金额',
  `address_id` int(11) NOT NULL COMMENT '收货地址编码',
  `pay_channel` tinyint(4) NOT NULL DEFAULT '0' COMMENT '支付渠道 0余额 1微信 2支付宝',
  `order_pay_no` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单支付单号',
  `third_pay_trade_no` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '第三方支付流水号',
  `pay_time` int(11) NOT NULL DEFAULT '0' COMMENT '付款时间',
  `delivery_time` int(11) NOT NULL DEFAULT '0' COMMENT '发货时间',
  `order_settlement_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '订单结算状态 0未结算 1已结算',
  `order_settlement_time` int(11) NOT NULL DEFAULT '0' COMMENT '订单结算时间',
  `created_time` timestamp NULL DEFAULT NULL,
  `updated_time` timestamp NULL DEFAULT NULL,
  `deleted_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_order_transaction_no_unique` (`order_transaction_no`),
  KEY `order_transaction_member_id_order_status_index` (`order_transaction_no`,`member_id`,`order_status`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


用户表：
CREATE TABLE t_customer(
  id INT UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '自增主键ID',
  customer_id INT UNSIGNED NOT NULL COMMENT '用户id',
  customer_name VARCHAR(20) NOT NULL COMMENT '用户真实姓名',
  identity_card_type TINYINT NOT NULL DEFAULT 1 COMMENT '证件类型：1 身份证，2 军官证，3 护照',
  identity_card_no VARCHAR(20) COMMENT '证件号码',
  mobile_phone INT UNSIGNED COMMENT '手机号',
  customer_email VARCHAR(50) COMMENT '邮箱',
  gender CHAR(1) COMMENT '性别',
  user_point INT NOT NULL DEFAULT 0 COMMENT '用户积分',
  register_time timestamp NOT NULL COMMENT '注册时间',
  birthday DATETIME COMMENT '会员生日',
  customer_level TINYINT NOT NULL DEFAULT 1 COMMENT '会员级别：1 普通会员，2 青铜，3白银，4黄金，5钻石',
  user_balance DECIMAL(8,2) NOT NULL DEFAULT 0.00 COMMENT '用户余额',
  modified_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY pk_customer_id(id)
) ENGINE = innodb COMMENT '用户信息表';

商品表
CREATE TABLE t_product(
  product_id INT UNSIGNED AUTO_INCREMENT NOT NULL COMMENT '商品ID',
  product_no VARCHAR(16) NOT NULL COMMENT '商品编码',
  product_name VARCHAR(20) NOT NULL COMMENT '商品名称',
  brand_id INT UNSIGNED NOT NULL COMMENT '品牌表的ID',
  supplier_id INT UNSIGNED NOT NULL COMMENT '商品的供应商ID',
  price DECIMAL(8,2) NOT NULL COMMENT '商品销售价格',
  publish_status TINYINT NOT NULL DEFAULT 0 COMMENT '上下架状态：0下架1上架',
  audit_status TINYINT NOT NULL DEFAULT 0 COMMENT '审核状态：0未审核，1已审核',
  production_date DATETIME NOT NULL COMMENT '生产日期',
  shelf_life INT NOT NULL COMMENT '商品有效期',
  descript TEXT NOT NULL COMMENT '商品描述',
  product_indate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '商品录入时间',
  modified_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY pk_productid(product_id)
) ENGINE = innodb COMMENT '商品信息表';


2 设计模式之前有过总结，这次复习了下，但是只来得急写创建型的5种模式
（1）简单工厂模式
（2）工厂方法模式
（3）抽象工厂模式
（4）建造者模式
（5）单例模式

3 工作中遇到mysql的问题
http://note.youdao.com/s/IG42UR9X

4 bytebuddy 简单实现 agent监控方法的耗时
工程： api-demo-agent

