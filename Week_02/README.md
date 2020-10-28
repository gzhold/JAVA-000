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
(4) java -jar -Xmx512m -Xms512m -XX:+UseG1GC gateway-server-0.0.1-SNAPSHOT.jar 

增加gc log
java -jar -Xmx1g -Xms1g -XX:+UseG1GC -Xloggc:gc.cms.log -XX:+PrintGC gateway-server-0.0.1-SNAPSHOT.jar
java -jar -Xmx1g -Xms1g -XX:+UseConcMarkSweepGC -Xloggc:gc.g1.log -XX:+PrintGC gateway-server-0.0.1-SNAPSHOT.jar


3、从验证现象和结果总结：
(1)分别压测cms和g1，发现内存在1g时，cms的ygc 表现更好，平均gc时间更少，内存占比也更少
(2)通过比较发现CMS类型的OldGC平均STW时间明显更少
(3) G1 初始分配内存更少，cms初始分配内存较多
(4) 在4G内存时，g1平均时间较少


