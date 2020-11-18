package sharding.plugin.strategy;

import sharding.plugin.annotation.Sharding;


/**
 * hash策略
 */
public class HashShardingStrategy extends AbstractShardingStrategy {


    /**
     * 计算分表
     *
     * @param sharding
     * @param shardingKey
     * @return
     */
    @Override
    public Integer calcTableSuffix(Sharding sharding, String shardingKey) {
        return Math.abs(shardingKey.hashCode()) % sharding.count();
    }
}
