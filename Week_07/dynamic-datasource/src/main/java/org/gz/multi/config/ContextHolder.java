package org.gz.multi.config;

import org.apache.commons.lang3.RandomUtils;
import org.gz.multi.strategy.DynamicDataSourceStrategy;
import org.gz.multi.strategy.LoadBalanceDynamicDataSourceStrategy;
import org.gz.multi.strategy.RandomDynamicDataSourceStrategy;

import java.util.ArrayList;
import java.util.List;

public class ContextHolder {

    private final static ThreadLocal<DataSourceType> currentDatasource = new ThreadLocal<>();

    private static  DynamicDataSourceStrategy strategy = new RandomDynamicDataSourceStrategy();


    public static void putDataSource(DataSourceType dataSourceType) {
        currentDatasource.set(dataSourceType);
    }

    public static DataSourceType getDataSource() {
        return currentDatasource.get();
    }

    /**
     * 清除当前数据源
     */
    public static void clear() {
        currentDatasource.remove();
    }

    /**
     * 设置从从库读取数据
     */
    public static void setSlave() {
//        if (RandomUtils.nextInt(0, 2) > 0) {
//            ContextHolder.putDataSource(DataSourceType.SLAVE_2);
//        } else {
//            ContextHolder.putDataSource(DataSourceType.SLAVE_1);
//        }
        List<DataSourceType> list = new ArrayList<>();
        list.add(DataSourceType.SLAVE_1);
        list.add(DataSourceType.SLAVE_2);
        ContextHolder.putDataSource(strategy.determineDataSource(list));
    }

}
