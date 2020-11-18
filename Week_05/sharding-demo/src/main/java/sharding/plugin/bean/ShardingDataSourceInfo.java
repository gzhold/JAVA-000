package sharding.plugin.bean;

import lombok.Data;
import sharding.plugin.strategy.ShardingDataBaseStrategy;


@Data
public class ShardingDataSourceInfo {


    /**
     * 分库策略
     */
    private ShardingDataBaseStrategy shardingDataBaseStrategy;

    /**
     * 分库数量
     */
    private Integer shardingCount;

}
