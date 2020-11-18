package sharding.plugin.strategy;

import sharding.plugin.annotation.Sharding;


/**
 * 默认策略
 * 使用shardingKey直接作为分表位
 */
public class DefaultShardingStrategy extends AbstractShardingStrategy {

    /**
     * 计算分表
     *
     * @param sharding
     * @param shardingKey
     * @return
     */
    @Override
    public Integer calcTableSuffix(Sharding sharding, String shardingKey) {
        return Integer.valueOf(shardingKey);
    }

}
