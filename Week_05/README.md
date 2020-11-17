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
     
     
3 AOP (工程 AOP-DEMO)
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
    
(5) [必做]实现自动配置 （工程 auto-config）
    代码实现：auto.config.AutoConfiguration
    测试 ：test.auto.config.StudentTest