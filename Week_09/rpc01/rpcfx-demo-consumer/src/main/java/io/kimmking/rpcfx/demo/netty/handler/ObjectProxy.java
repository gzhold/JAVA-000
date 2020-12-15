package io.kimmking.rpcfx.demo.netty.handler;

import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.SerializableFunction;
import io.kimmking.rpcfx.demo.netty.client.ConnectionManager;
import io.kimmking.rpcfx.demo.netty.client.RpcfxFuture;
import io.kimmking.rpcfx.demo.netty.client.RpcfxService;
import io.kimmking.rpcfx.util.ServiceUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

public class ObjectProxy<T, P> implements InvocationHandler, RpcfxService<T, P, SerializableFunction<T>> {
    private Class<T> clazz;
    private String version;

    public ObjectProxy(Class<T> clazz, String version) {
        this.clazz = clazz;
        this.version = version;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class == method.getDeclaringClass()) {
            String name = method.getName();
            if ("equals".equals(name)) {
                return proxy == args[0];
            } else if ("hashCode".equals(name)) {
                return System.identityHashCode(proxy);
            } else if ("toString".equals(name)) {
                return proxy.getClass().getName() + "@" +
                            Integer.toHexString(System.identityHashCode(proxy)) +
                            ", with InvocationHandler " + this;
            } else {
                throw new IllegalStateException(String.valueOf(method));
            }
        }

        RpcfxRequest request = new RpcfxRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setServiceClass(method.getDeclaringClass().getName());
        request.setMethod(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParams(args);
        request.setVersion(version);


        String serviceKey = ServiceUtil.makeServiceKey(method.getDeclaringClass().getName(), version);
        RpcClientHandler handler = ConnectionManager.getInstance().chooseHandler(serviceKey);
        RpcfxFuture rpcFuture = handler.sendRequest(request);
        return rpcFuture.get();
    }

    @Override
    public RpcfxFuture call(String funcName, Object... args) throws Exception {
        String serviceKey = ServiceUtil.makeServiceKey(this.clazz.getName(), version);
        RpcClientHandler handler = ConnectionManager.getInstance().chooseHandler(serviceKey);
        RpcfxRequest request = createRequest(this.clazz.getName(), funcName, args);
        RpcfxFuture rpcFuture = handler.sendRequest(request);
        return rpcFuture;
    }

    @Override
    public RpcfxFuture call(SerializableFunction<T> tSerializableFunction, Object... args) throws Exception {
        String serviceKey = ServiceUtil.makeServiceKey(this.clazz.getName(), version);
        RpcClientHandler handler = ConnectionManager.getInstance().chooseHandler(serviceKey);
        RpcfxRequest request = createRequest(this.clazz.getName(), tSerializableFunction.getName(), args);
        RpcfxFuture rpcFuture = handler.sendRequest(request);
        return rpcFuture;
    }

    private RpcfxRequest createRequest(String className, String methodName, Object[] args) {
        RpcfxRequest request = new RpcfxRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setServiceClass(className);
        request.setMethod(methodName);
        request.setParams(args);
        request.setVersion(version);
        Class[] parameterTypes = new Class[args.length];
        // Get the right class type
        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = getClassType(args[i]);
        }
        request.setParameterTypes(parameterTypes);

        return request;
    }

    private Class<?> getClassType(Object obj) {
        Class<?> classType = obj.getClass();
        return classType;
    }

}
