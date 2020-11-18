package sharding.plugin.interceptor;


import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sharding.plugin.annotation.Sharding;
import sharding.plugin.common.RequestContext;
import sharding.plugin.strategy.DefaultShardingStrategy;
import sharding.plugin.strategy.HashShardingStrategy;
import sharding.plugin.strategy.ShardingStrategy;
import sharding.plugin.config.ShardingTableConfiguration;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


/**
 * mybatis插件
 * 通过mybatis插件对SQL进行拦截，需要在创建Statement之前进行拦截
 * @see ShardingTableConfiguration
 */
@Intercepts({@Signature(method = "prepare", type = StatementHandler.class, args = {Connection.class, Integer.class})})
public class ShardingInterceptor implements Interceptor {

    private final static Logger logger = LoggerFactory.getLogger(ShardingInterceptor.class);

    private static final String DELEGATE_BOUND_SQL_SQL = "delegate.boundSql.sql";
    private static final String DELEGATE_MAPPED_STATEMENT_ID = "delegate.mappedStatement.id";
    private static final String DELEGATE_PARAMETER_HANDLER_PARAMETER_OBJECT = "delegate.parameterHandler.parameterObject";
    private static final String PARAM_1 = "param1";
    private static final String POINT = ".";

    private static final ShardingStrategy DEFAULT_SHARDING_STRATEGY = new HashShardingStrategy();
//    private static final ShardingStrategy DEFAULT_SHARDING_STRATEGY = new DefaultShardingStrategy();

    private static final Map<String, ShardingStrategy> SHARDING_STRATEGY_MAP = Maps.newConcurrentMap();


    /**
     * 获取当前sql对应的mybatis元信息，从元信息中获取对应mapper接口上的标记注解，用来获取分库分表信息，进行SQL的重写
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);

        String id = (String) metaObject.getValue(DELEGATE_MAPPED_STATEMENT_ID);
        String className = id.substring(0, id.lastIndexOf(POINT));
        // 获取注解的信息
        Sharding sharding = Class.forName(className).getDeclaredAnnotation(Sharding.class);
        if (sharding != null && sharding.sharding()) {
            String sql = (String) metaObject.getValue(DELEGATE_BOUND_SQL_SQL);
            sql = sql.replaceAll(sharding.tableName(), getTargetTableName(metaObject, sharding));
            metaObject.setValue(DELEGATE_BOUND_SQL_SQL, sql);
        }
        Stopwatch stopwatch = Stopwatch.createStarted();
        Object proceedReslut = invocation.proceed();
        stopwatch.stop();
        if (logger.isInfoEnabled()) {
            logger.info("Sql execute {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        }
        return proceedReslut;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
        logger.info("Do nothing!");
    }

    private String getTargetTableName(MetaObject metaObject, Sharding sharding) throws Exception {
        String shardingKey = getShardingKey(metaObject);
        String targetTableName;
        if (!StringUtils.isEmpty(shardingKey)) {
            targetTableName = getShardingStrategy(sharding).getTargetTableName(sharding, shardingKey);
        } else if (StringUtils.isEmpty(shardingKey) && !StringUtils.isEmpty(RequestContext.getShardingTable())) {
            targetTableName = DEFAULT_SHARDING_STRATEGY.getTargetTableName(sharding, RequestContext.getShardingTable());
        } else {
            throw new RuntimeException("Not query data, shardingKey=" + shardingKey + "，ShardingContext=" + RequestContext.getShardingTable());
        }
        return targetTableName;
    }

    private ShardingStrategy getShardingStrategy(Sharding sharding) throws Exception {
        String strategyClassName = sharding.strategy();
        ShardingStrategy shardingStrategy = SHARDING_STRATEGY_MAP.get(strategyClassName);
        if (shardingStrategy == null) {
            ShardingStrategy strategy = (ShardingStrategy) Class.forName(strategyClassName).newInstance();
            SHARDING_STRATEGY_MAP.putIfAbsent(strategyClassName, strategy);
            shardingStrategy = SHARDING_STRATEGY_MAP.get(strategyClassName);
        }
        return shardingStrategy;
    }

    /**
     * 默认取第一个参数作为分表键
     * @param metaObject
     * @return
     */
    private String getShardingKey(MetaObject metaObject) {
        String shardingKey = null;
        Object parameterObject = metaObject.getValue(DELEGATE_PARAMETER_HANDLER_PARAMETER_OBJECT);
        if (parameterObject instanceof String) {
            shardingKey = (String) parameterObject;
        } else if (parameterObject instanceof Map) {
            Map<String, Object> parameterMap = (Map<String, Object>) parameterObject;
            Object param1 = parameterMap.get(PARAM_1);
            if (param1 instanceof String) {
                shardingKey = (String) param1;
            }
        }
        return shardingKey;
    }

    private Object realTarget(Object target) {
        if (Proxy.isProxyClass(target.getClass())) {
            MetaObject metaObject = SystemMetaObject.forObject(target);
            return realTarget(metaObject.getValue("h.target"));
        }
        return target;
    }
}
