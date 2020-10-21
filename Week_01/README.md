学习笔记

1、自定义classloader
2、jvm运行时内存图
3、JVM参数
-Xmx2048m -Xms2048m -Xmn1024m -XX:MetaspaceSize=256m -Xss256K -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=70 -XX:+UseCMSInitiatingOccupancyOnly -XX:+DisableExplicitGC 
-XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:+PrintGCApplicationStoppedTime -XX:+CMSScavengeBeforeRemark -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintHeapAtGC 
-Xloggc:/opt/uyun/itsm/ticket/logs/gc.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/opt/uyun/itsm/ticket/logs/heapDumpOOM.log

在原来的基础上加了一个参数
-XX:+PrintGCApplicationStoppedTime 打印垃圾回收期间程序暂停的时间
