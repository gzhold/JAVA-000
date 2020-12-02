package org.gz.sharding.config;


import com.alibaba.druid.pool.DruidDataSource;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.algorithm.ShardingSphereAlgorithmConfiguration;
import org.apache.shardingsphere.replicaquery.api.config.ReplicaQueryRuleConfiguration;
import org.apache.shardingsphere.replicaquery.api.config.rule.ReplicaQueryDataSourceRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;


@Configuration
@MapperScan(basePackages = "org.gz.sharding.mapper")
@Slf4j
public class DataSourceConfig {

    @Bean
    public DataSource dataSource() {
        // 配置真实数据源
        Map<String, DataSource> dataSourceMap = new HashMap<>();

        // 配置第 1 个数据源
        DruidDataSource dataSource1 = new DruidDataSource();
        dataSource1.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource1.setUrl("jdbc:mysql://localhost:3306/kad?useSSL=false&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&allowMultiQueries=true");
        dataSource1.setUsername("root");
        dataSource1.setPassword("Admin@123");
        dataSourceMap.put("ds0", dataSource1);

        // 配置第 2 个数据源
        DruidDataSource dataSource2 = new DruidDataSource();
        dataSource2.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource2.setUrl("jdbc:mysql://localhost:3306/test?useSSL=false&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&allowMultiQueries=true");
        dataSource2.setUsername("root");
        dataSource2.setPassword("Admin@123");
        dataSourceMap.put("ds1", dataSource2);

        //配置读写分离规则
        List<ReplicaQueryDataSourceRuleConfiguration> configurations = new ArrayList<>();
        configurations.add(new ReplicaQueryDataSourceRuleConfiguration("ds", "ds0", Lists.newArrayList("ds1"), "load_balancer"));
        Map<String, ShardingSphereAlgorithmConfiguration> loadBalancers = new HashMap<>();
        loadBalancers.put("load_balancer", new ShardingSphereAlgorithmConfiguration("ROUND_ROBIN", new Properties()));
        ReplicaQueryRuleConfiguration ruleConfiguration = new ReplicaQueryRuleConfiguration(configurations, loadBalancers);


        DataSource dataSource = null;
        try {
            dataSource = ShardingSphereDataSourceFactory.createDataSource(dataSourceMap, Collections.singleton(ruleConfiguration), new Properties());
        } catch (SQLException e) {
            log.error("create datasource failed, error is {}", e);
        }

        log.info("datasource : {}", dataSource);
        return dataSource;
    }


}
