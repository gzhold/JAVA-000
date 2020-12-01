package org.gz.multi.strategy;

import org.gz.multi.config.DataSourceType;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


/**
 * 随机
 */
public class RandomDynamicDataSourceStrategy implements DynamicDataSourceStrategy {


    @Override
    public DataSourceType determineDataSource(List<DataSourceType> dataSources) {
        return dataSources.get(ThreadLocalRandom.current().nextInt(dataSources.size()));
    }

}
