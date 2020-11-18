package sharding.plugin.config;


import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import sharding.plugin.bean.Database;
import sharding.plugin.bean.ShardingDataSourceInfo;
import sharding.plugin.common.RequestContext;
import sharding.plugin.strategy.AbstractShardingStrategy;
import sharding.plugin.strategy.ShardingDataBaseStrategy;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Set;


/**
 * 装配分库分表插件
 */
@Configuration
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@ConditionalOnProperty(name = "sharding.databases", havingValue = "enable")
@EnableConfigurationProperties(ShardingProperties.class)
public class ShardingDataSourceConfiguration {

    @Autowired
    private ShardingProperties shardingProperties;

    private DataSource shardingDataSource() {
        Map<String, Database> databases = shardingProperties.getDatabases();
        Preconditions.checkArgument(null != databases && databases.size() > 0, "不存在分库配置");

        Map<String, ShardingDataSourceInfo> shardingDataSourceInfoMap = Maps.newConcurrentMap();
        Map<Object, Object> targetDataSources = Maps.newConcurrentMap();
        DataSource dataSource = null;

        for (Map.Entry<String, Database> entry : databases.entrySet()) {
            String dataBaseName = entry.getKey();
            Database database = entry.getValue();

            ShardingDataSourceInfo shardingDataSourceInfo = new ShardingDataSourceInfo();
            shardingDataSourceInfo.setShardingCount(database.getShardingCount());
            shardingDataSourceInfo.setShardingDataBaseStrategy(createShardingDataBaseStrategy(database.getShardingStrategy()));
            shardingDataSourceInfoMap.put(dataBaseName, shardingDataSourceInfo);

            Set<Map.Entry<String, Map<Integer, DataSourceProperties>>> entries = database.getDataSource().entrySet();
            for (Map.Entry<String, Map<Integer, DataSourceProperties>> masterSlave : entries) {
                String masterSlaveKey = masterSlave.getKey();
                Map<Integer, DataSourceProperties> masterSlaveValue = masterSlave.getValue();
                for (Map.Entry<Integer, DataSourceProperties> propertiesEntry : masterSlaveValue.entrySet()) {
                    String shardingDataBaseKey = dataBaseName + masterSlaveKey + propertiesEntry.getKey();
                    dataSource = createDataSource(propertiesEntry.getValue(), HikariDataSource.class);
                    targetDataSources.put(shardingDataBaseKey, dataSource);
                }
            }
        }

        Preconditions.checkArgument(null != targetDataSources && targetDataSources.size() > 0, "找不到database配置");
        Preconditions.checkNotNull(dataSource, "找不到database配置");

        AbstractShardingStrategy.setShardingDataSourceInfoMap(shardingDataSourceInfoMap);

        ShardingDataSource shardingDataSource = new ShardingDataSource();
        shardingDataSource.setTargetDataSources(targetDataSources);
        /**
         * 用于创建LazyConnectionDataSourceProxy时获取真实数据库连接，来获取实际数据库的自动提交配置和隔离级别
         */
        shardingDataSource.setDefaultTargetDataSource(dataSource);
        shardingDataSource.setLenientFallback(false);
        shardingDataSource.afterPropertiesSet();
        return shardingDataSource;
    }

    @Bean
    public DataSource dataSource() {
        LazyConnectionDataSourceProxy dataSourceProxy = new LazyConnectionDataSourceProxy();
        dataSourceProxy.setTargetDataSource(shardingDataSource());
        return dataSourceProxy;
    }

    @SuppressWarnings("unchecked")
    private <T> T createDataSource(DataSourceProperties properties,
                                   Class<? extends DataSource> type) {
        return (T) properties.initializeDataSourceBuilder().type(type).build();
    }

    private ShardingDataBaseStrategy createShardingDataBaseStrategy(String shardingDataBaseStrategyClassName) {
        try {
            return (ShardingDataBaseStrategy) Class.forName(shardingDataBaseStrategyClassName).newInstance();
        } catch (Exception e) {
            throw new RuntimeException("初始化ShardingDataBaseStrategy失败。ShardingDataBaseStrategy=" + shardingDataBaseStrategyClassName);
        }
    }

    /**
     * 根据分库上下文路由DataSource
     *
     * @author kris
     */
    public static class ShardingDataSource extends AbstractRoutingDataSource {

        /**
         * ShardingContext.getShardingDatabase() 为库名+分库序号
         *
         * @return
         */
        @Override
        protected Object determineCurrentLookupKey() {
            return RequestContext.getShardingDatabase();
        }
    }

}
