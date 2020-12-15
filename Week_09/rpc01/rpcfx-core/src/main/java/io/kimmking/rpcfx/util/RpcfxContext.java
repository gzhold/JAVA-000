package io.kimmking.rpcfx.util;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RpcfxContext {
    public static final Map<String, Object> serviceMap = new ConcurrentHashMap<>();


    public static void addService(String interfaceName, Object serviceBean) {
        serviceMap.put(interfaceName, serviceBean);
    }

    public static void clear () {
        serviceMap.clear();;
    }

    public static Object getServiceBean(String interfaceName) {
        return serviceMap.get(interfaceName);
    }

}
