学习笔记

机器： Mac Pro  8C 16G

1、内存大小分别用了 256M 512M 1024M 2048M 4096M
-Xmx256M -XX:+UseG1GC -XX:+PrintGC  -XX:+PrintGCDetails  -XX:+PrintGCTimeStamps -Xloggc:gc.log

GC策略
-XX:+UseSerialGC
-XX:+UseParallelGC
-XX:+UseConcMarkSweepGC
-XX:+UseG1GC

(1)在堆配置256M时， 发现cms未发生OOM，其他gc策略都发生了OOM
(2)配置512M及以上堆内存，都未发生OOM。
(3)在1G堆时，配置CMS 创建的对象最多，G1 从几次压测来看，没有CMS，PS表现好
(4)G1在4G堆表现最好
(5)小堆 PS或CMS 都表现的可以，一般用的多还是cms，大堆可以考虑用G1


2、512M内存 不同GC压测 （堆调整了 1G，2G，4G）
http://note.youdao.com/s/FOZfvDeA

(1) java -jar -Xmx512m -Xms512m -XX:+UseSerialGC gateway-server-0.0.1-SNAPSHOT.jar 
(2) java -jar -Xmx512m -Xms512m -XX:+UseConcMarkSweepGC gateway-server-0.0.1-SNAPSHOT.jar 
(3) java -jar -Xmx512m -Xms512m -XX:+UseParallelGC gateway-server-0.0.1-SNAPSHOT.jar 
(4) java -jar -Xmx512m -Xms512m -XX:+UseSerialGC gateway-server-0.0.1-SNAPSHOT.jar 






