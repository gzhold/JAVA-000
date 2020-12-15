package io.kimmking.rpcfx.demo.netty.server;

import io.kimmking.rpcfx.annotation.RpcfxService;
import io.kimmking.rpcfx.util.RpcfxContext;
import io.kimmking.rpcfx.util.ServiceUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;

import java.util.Map;

public class RpcServer extends NettyServer implements ApplicationContextAware, InitializingBean, DisposableBean {

    public RpcServer(String serverAddress, String registryAddress) {
        super(serverAddress, registryAddress);
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        Map<String, Object> serviceBeanMap = ctx.getBeansWithAnnotation(RpcfxService.class);
        if (!CollectionUtils.isEmpty(serviceBeanMap)) {
            for (Object serviceBean : serviceBeanMap.values()) {
                RpcfxService nettyRpcService = serviceBean.getClass().getAnnotation(RpcfxService.class);
                String interfaceName = nettyRpcService.value().getName();
                String key = ServiceUtil.makeServiceKey(interfaceName, nettyRpcService.version());
                RpcfxContext.addService(key, serviceBean);
            }
        }
    }

    @Override
    public void destroy() throws Exception {
        RpcfxContext.clear();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.start();
    }
}
