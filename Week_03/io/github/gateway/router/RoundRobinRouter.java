package io.github.gateway.router;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RoundRobinRouter implements HttpEndpointRouter {
    // ip address
    private final Map<String, Integer> serverMap = new ConcurrentHashMap<>();

    private static Integer pos = 0;

    @Override
    public String route(List<String> endpoints) {

        serverMap.putAll(IpMap.serverWeightMap);

        // 取得Ip地址List
        Set<String> keySet = serverMap.keySet();
        ArrayList<String> keyList = new ArrayList<>(keySet);
        String server = null;
        synchronized (pos) {
            if (pos > keySet.size()) {
                pos = 0;
            }
            server = keyList.get(pos);
            pos ++;
        }
        // 测试数据
        System.out.println("Test server is = " + server);
        // 真实接口
        return endpoints.get(0);
    }


    /**
     * 模拟数据
     */
    static class IpMap {
        // 待路由的Ip列表，Key代表Ip，Value代表该Ip的权重
        public static Map<String, Integer> serverWeightMap = new ConcurrentHashMap<>();

        static {
            // 权重为1
            serverWeightMap.put("192.168.1.100", 1);
            // 权重为4
            serverWeightMap.put("127.0.0.1", 4);
            // 权重为3
            serverWeightMap.put("192.168.1.105", 3);
            // 权重为2
            serverWeightMap.put("192.168.1.107", 2);
            serverWeightMap.put("192.168.1.110", 1);
        }
    }
}
