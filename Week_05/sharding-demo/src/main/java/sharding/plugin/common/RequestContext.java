package sharding.plugin.common;

public class RequestContext {

    /**
     * 分库key 库名+分库序号
     * 用于获取对应库名序号的dataSource
     * 使用分库插件时必须及时调用clear方法清空上下文
     */
    private static final ThreadLocal<String> SHARDING_DATABASE = new ThreadLocal<>();

    /**
     * 分表Key 表名后缀
     * 直接填充分表位，主要用于按表进行扫描，使用完成后必须及时调用clear方法清空上下文
     */
    private static final ThreadLocal<String> SHARDING_TABLE = new ThreadLocal<>();

    /**
     * MASTER OR SLAVE
     * 建议通过切面设置强制读从库，DB操作执行完成后在切面执行clearSlave方法，清除主从上下文
     */
    private static final ThreadLocal<String> MASTER_SALVE = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return Constants.MASTER;
        }
    };

    public static String getShardingDatabase() {
        return SHARDING_DATABASE.get();
    }

    public static void setShardingDatabase(String shardingDatabase) {
        SHARDING_DATABASE.set(shardingDatabase);
    }

    public static String getShardingTable() {
        return SHARDING_TABLE.get();
    }

    public static void setShardingTable(String shardingTable) {
        SHARDING_TABLE.set(shardingTable);
    }

    public static void forceSlave() {
        MASTER_SALVE.set(Constants.SLAVE);
    }

    public static void clearSlave() {
        MASTER_SALVE.remove();
    }

    public static String getMasterSalve() {
        return MASTER_SALVE.get();
    }

    public static void clearAll() {
        RequestContext.SHARDING_DATABASE.remove();
        RequestContext.SHARDING_TABLE.remove();
        RequestContext.MASTER_SALVE.remove();
    }

}
