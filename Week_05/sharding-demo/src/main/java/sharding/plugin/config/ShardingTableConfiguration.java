package sharding.plugin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sharding.plugin.interceptor.ShardingInterceptor;


@Configuration
public class ShardingTableConfiguration {

    @Bean
    public ShardingInterceptor shardingInterceptor() {
        return new ShardingInterceptor();
    }

}
