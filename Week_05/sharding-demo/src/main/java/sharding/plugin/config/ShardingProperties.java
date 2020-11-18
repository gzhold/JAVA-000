package sharding.plugin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import sharding.plugin.bean.Database;

import java.util.Map;


@Configuration
@ConfigurationProperties(prefix = "sharding")
@Data
public class ShardingProperties {

    private Map<String, Database> databases;
}
