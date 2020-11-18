package sharding.plugin.bean;


import lombok.Data;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import java.util.Map;


@Data
public class Database {

    /**
     * 分库策略
     */
    private String shardingStrategy = "";

    /**
     * 分库数量
     */
    private Integer shardingCount;

    /**
     * key：分库位
     * value：分库对应的dataSource配置
     */
    private Map<String, Map<Integer, DataSourceProperties>> dataSource;
}
