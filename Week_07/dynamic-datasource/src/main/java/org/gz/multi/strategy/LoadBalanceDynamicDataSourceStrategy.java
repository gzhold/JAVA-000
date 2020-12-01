package org.gz.multi.strategy;

import org.gz.multi.config.DataSourceType;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class LoadBalanceDynamicDataSourceStrategy implements DynamicDataSourceStrategy {

    /**
     * 负载均衡计数器
     */
    private final AtomicInteger index = new AtomicInteger(0);

    @Override
    public DataSourceType determineDataSource(List<DataSourceType> dataSources) {
        return dataSources.get(Math.abs(index.getAndAdd(1) % dataSources.size()));
    }


}
