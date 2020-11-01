gateway 作业

1 自定义handler ： SyncHttpOutboundHandler
用httpclient  实现调用接口

2 自定义过滤器：AccessLogFilter
打印header参数
在header 加上参数 nio:gzhold

3 自定义路由 RandomRouter

在LoadBalance 处理业务，实现路由规则

4 在server中，（HelloController类）获取header中自定义参数