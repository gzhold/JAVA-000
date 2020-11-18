package sharding.plugin.annotation;


import sharding.plugin.strategy.ShardingStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 描述：由于需要知道哪些表的SQL需要被拦截重写，一般可以通过注解进行元数据的标记，
 *   定义如下注解，并将该注解标记在mybatis的mapper接口上，便可以在拦截器中获取分库分表规则。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Sharding {

    /**
     * 是否分库分表
     * @return
     */
    boolean sharding() default false;

    /**
     * 数据库
     * @return
     */
    String databaseName();

    /**
     * 表名
     * @return
     */
    String tableName();

    /**
     * @see ShardingStrategy
     * 分表策略
     */
    String strategy();

    /**
     * 分表数量
     */
    int count();

}
