package sharding.plugin.strategy;

public class DefaultShardingDatabaseStrategy implements ShardingDataBaseStrategy {

    @Override
    public Integer calculate(int sharingDataBaseCount, int sharingTableCount, int currentShardingTableKey) {
        if (sharingTableCount >= sharingDataBaseCount && sharingTableCount % sharingDataBaseCount == 0) {
            int base = sharingTableCount / sharingDataBaseCount;
            return currentShardingTableKey / base;
        }
        throw new RuntimeException("Incorrect configuration of sub-database and sub-table rules!");
    }

}
