package sharding.plugin.strategy;

import com.google.common.collect.Maps;
import lombok.Setter;
import sharding.plugin.annotation.Sharding;
import sharding.plugin.bean.ShardingDataSourceInfo;
import sharding.plugin.common.RequestContext;

import java.util.Map;

public abstract class AbstractShardingStrategy implements ShardingStrategy {

    @Setter
    private static Map<String, ShardingDataSourceInfo> shardingDataSourceInfoMap = Maps.newConcurrentMap();

    /**
     * 获取目标表名
     *
     * @param sharding
     * @param shardingKey
     * @return
     */
    @Override
    public String getTargetTableName(Sharding sharding, String shardingKey) {
        Integer tableSuffix = calcTableSuffix(sharding, shardingKey);
        ShardingDataSourceInfo shardingDataSourceInfo = shardingDataSourceInfoMap.get(sharding.databaseName());
        if (shardingDataSourceInfo != null) {
            int databaseNum = shardingDataSourceInfo.getShardingDataBaseStrategy().calculate(shardingDataSourceInfo.getShardingCount(), sharding.count(), tableSuffix);
            RequestContext.setShardingDatabase(sharding.databaseName() + RequestContext.getMasterSalve() + databaseNum);
        }
        return getTableName(sharding.tableName(), tableSuffix);
    }

    private String getTableName(String tableName, Integer shardingKey) {
        return tableName + UNDERLINE + shardingKey;
    }
}
