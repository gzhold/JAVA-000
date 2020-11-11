1 线程池和CompletableFuture 实现 (PrintThreadReturn.java)
（1）CompletableFuture
（2）自定义线程池

2 常用的并发工具类实现 (ConcurrentToolsDemo.java)
 (1) CountDownLatch
 (1) CyclicBarrier
 (1) Semaphore
 
3 工作中并发有关场景
 (1) 创建工单接口，出现多次点击事件，重复提交
    A 页面触发一次后置灰
    B 打开页面就生成id，接口重复校验
    
 (2) 修改表单页面
    A 数据库版本号 -- 乐观锁
    
 (3) 告警派单，关联业务问题导致调用我们接口频繁
    接口限流 -- redis+lua 实现
