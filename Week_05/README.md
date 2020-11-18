1 [必做] bean的装配 (工程 spring-bean)
（1）xml配置bean，通过applicationContext.getBean() 获取
    BeanInstantiationDemo
 (2)xml配置 factory-method，通过静态方法获取bean
    BeanInstantiationByStaticMethodDemo
 (3)RegistryBeanDefinition 构建BeanDefinition
    RegistryBeanDefinitionDemo
 (4)自定义BeanFactory，实例化Bean
    BeanFactoryDemo
 (5)FactoryBean#getObject()获取bean
    FactoryBeanDemo
 (6) 注解获取
    BeanAnnotationDemo
    
2 [必做]jdbc (工程 JDBC-DEMO)

  demo中是在之前的netty-server 和 gateway 示例基础上改造的
  
 (1) 使用 JDBC 原生接口，实现数据库的增删改查操作 （实现类：HttpHandler）
    public ResultSet queryByStatement(Connection connection, String sql) throws SQLException {
        assert connection != null;
        Statement stmt = connection.createStatement();
        return stmt.executeQuery(sql);
    }
 (2)使用事务，PrepareStatement 方式，批处理方式（实现类：HttpHandler）
    // 流未关闭 
    public ResultSet batchUpdateByPreparedStatement(Connection connection) throws SQLException {
         assert connection != null;
         PreparedStatement ps = connection.prepareStatement("insert into account (id,name,balance) values(?,?,?)");
         connection.setAutoCommit(false);
         for (int i = 0; i < 2; i++) {
             ps.setInt(1, getUUID(5));
             ps.setString(2, "张三");
             ps.setBigDecimal(3, new BigDecimal(80.8));
             ps.addBatch();
         }
         ps.executeBatch();
         // 提交
         connection.commit();
         // 还原
         connection.setAutoCommit(true);
         // 查询
         ps = connection.prepareStatement("SELECT id, name, balance FROM account");
         return ps.executeQuery();
     }
 (3) 通过spring-boot 整合 Hikari
     @Autowired
     private DataSource dataSource;
 
     @GetMapping("/user")
     public String getUser(HttpServletRequest request){
         StringBuilder builder = new StringBuilder();
         Connection connection = null;
         try {
             connection = dataSource.getConnection();
             String sql = "SELECT id, name, balance FROM account";
             ResultSet rs = queryByStatement(connection, sql);
 
             while (rs.next()){
                 int id = rs.getInt("id");
                 String name = rs.getString("name");
                 BigDecimal balance =  rs.getBigDecimal("balance");
 //                System.out.println("id = " + id + ", name = " + name + ", balance = " + balance);
                 builder.append("id = ");
                 builder.append(id);
                 builder.append(", name = ");
                 builder.append(name);
                 builder.append(", balance = ");
                 builder.append(balance);
                 builder.append("\r\n");
             }
         } catch (SQLException e) {
             e.printStackTrace();
         } finally {
            try{
                 if(connection!=null) {
                     connection.close();
                 }
             }catch(SQLException se){
                 se.printStackTrace();
             }
         }
         return builder.toString();
     }
 
     private ResultSet queryByStatement(Connection connection, String sql) throws SQLException {
         assert connection != null;
         Statement stmt = connection.createStatement();
         return stmt.executeQuery(sql);
     }
     
     
3 [选做] AOP (工程 AOP-DEMO)
(1) 装饰器模式，不修改原始类的逻辑
    public class LogDecorator implements UserService {
    
        private UserService delegate;
    
        public LogDecorator(UserService delegate) {
            this.delegate = delegate;
        }
    
        public String query(String id) {
            System.out.println("Log-query user, id is " + id);
            return delegate.query(id);
        }
    }
    
(2) jdk动态代理
    public class LogProxy implements InvocationHandler {
    
        private UserService delegate;
    
        public LogProxy(UserService delegate) {
            this.delegate = delegate;
        }
    
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println(proxy.getClass().getSimpleName() + "-" + method.getName() + " is invoked: " + Arrays.toString(args));
            Object result = method.invoke(delegate, args);
            System.out.println(method.getName() + " is finished: " + result);
            return result;
        }
    }


(3) 将指定类中所有带有 @MyLog 注解 ( 自定义注解)的方法都过滤出来 - ByteBuddy
    代码实现：包test.aop.bytebuddy.log

(4) 基于 AOP 和自定义注解，实现 @MyCache(60) 对于指定方法返回值缓存 60 秒 - ByteBuddy
    代码实现：包test.aop.bytebuddy.cache
    
    可自定义失效时间，通过注解配置
    
4 [必做]实现自动配置 （工程 auto-config）
    代码实现：auto.config.AutoConfiguration
    测试 ：test.auto.config.StudentTest
    

5 [选做] 基于 MyBatis 实现一个简单的分库分表 + 读写分离 + 分布式 ID 生成方案（工程 sharding-demo）

分表优缺点

 (1)垂直拆分：
 垂直拆分分表指的是将一张大表根据业务、字段冷热、大字段等因素，拆分成多个结构不同的表。
 
 优点：
 优化查询性能，减少IO消耗。数据库索引通常以页位单位加载数据，单行数据越小，一页中包含的数据就越多，内存能加载更多数据，命中率更高。
 
 缺点：
 产生多表之间的关联查询，一般在业务层面进行多表数据组装，增加了一定的复杂性。
 
 (2)水平拆分：
 水平拆分分表指的是按一定的分片算法，将同一张表的数据拆分到多个表，每个表结构相同。
 
 优点：
  降低单表数据量，优化查询性能，一般来说MYSQL建议单表数据量在1000W以内，一般预估数据有效时间或热点时间内的数据量单表不超过1000W即可，历史数据进行归档。

 缺点：
  若使用场景存在多个分表键，往往需要根据各个分表键进行一定的数据冗余存储。冗余一般分为冗余索引关系表和冗余全量表，需要根据性能，存储成本，维护成本等方面进行选择。
  复杂查询支持度不高。一般分表会通过某个业务键比如uid进行分表，非uid维度的查询，比如进行一些聚合查询，分页查询，排序，统计等SQL，将难以执行。
  解决方案一般通过添加额外存储结构进行处理，常用的ES+HBase方案，通过ES将索引字段进行存储，通过主键或唯一键，关联到HBase进行全量数据查询查询。


分库的优缺点

(1)垂直拆分：
垂直拆分分库指的是根据业务模块，将不同业务、关联度不高的表拆分到不同的数据库中，关联度高的表集中在一个库。

优点：
业务隔离，不同业务的库中只包含该业务所属的表，减少业务系统相互之间的影响。
优化数据库库性能，减少数据库压力，避免磁盘存储容量不足。

缺点：
跨库的复杂查询，需要业务层面进行数据组装，增加了复杂性。
跨库的事务引起的分布式事务问题。解决方案见：分布式事务。不过还是建议一般情况下通过一定的系统设计，避免分布式事务的问题。

(2)水平拆分：
水平拆分分库指的是按一定的分片算法，先将同一张表的数据拆分到多个表，每个表结构相同，再将这多个表拆分到多个数据库中。

优点：
优化单机和单库的性能瓶颈，比如CPU、IO、内存等瓶颈，提高数据库集群整体的吞吐量，提高稳定性。
避免磁盘存储容量不足。

缺点：
若使用场景存在多个分表键，往往需要根据各个分表键进行一定的数据冗余存储。冗余一般分为冗余索引关系表和冗余全量表，需要根据性能，存储成本，维护成本等方面进行选择。
复杂查询支持度不高。一般分库分表会通过某个业务键比如uid进行分库分表，非uid维度的查询，比如进行一些聚合查询，分页查询，排序，统计等SQL，将难以执行，一般通过添加额外存储结构进行处理，
常用的ES+HBase方案，通过ES将索引字段进行存储，通过主键或唯一键，关联到HBase进行全量数据查询查询。
跨分片的事务引起的分布式事务问题。解决方案见：分布式事务。不过还是建议一般情况下通过一定的系统设计，避免分布式事务的问题。