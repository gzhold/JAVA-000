package org.gz.multi.strategy;

import org.gz.multi.config.DataSourceType;

import java.util.List;

public interface DynamicDataSourceStrategy {

    /**
     * determine a database from the given dataSources
     *
     * @param dataSources given dataSources
     * @return final dataSource
     */
    DataSourceType determineDataSource(List<DataSourceType> dataSources);

}
