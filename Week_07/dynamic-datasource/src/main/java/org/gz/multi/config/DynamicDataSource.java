package org.gz.multi.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;


@Log4j2
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        log.info("当前数据源是 {}", ContextHolder.getDataSource());
        if (null == ContextHolder.getDataSource()) {
            ContextHolder.setSlave();
        }
        return ContextHolder.getDataSource();
    }
}
