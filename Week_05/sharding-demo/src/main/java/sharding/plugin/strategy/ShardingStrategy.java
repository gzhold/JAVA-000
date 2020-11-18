package sharding.plugin.strategy;

import sharding.plugin.annotation.Sharding;


/**
 * 分表策略
 */
public interface ShardingStrategy {

    String UNDERLINE = "_";

    /**
     * 获取目标表名
     * @param sharding
     * @param shardingKey
     * @return 带分表位的实际表名
     */
    String getTargetTableName(Sharding sharding, String shardingKey);


    /**
     * 计算分表
     * @param sharding
     * @param shardingKey
     * @return 计算分表
     */
    Integer calcTableSuffix(Sharding sharding, String shardingKey);

}
