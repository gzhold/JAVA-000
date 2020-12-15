package io.kimmking.rpcfx.demo.netty.client;

import io.kimmking.rpcfx.api.SerializableFunction;

public interface RpcfxService<T, P, FN extends SerializableFunction<T>> {

    RpcfxFuture call(String funcName, Object... args) throws Exception;

    /**
     * lambda method reference
     */
    RpcfxFuture call(FN fn, Object... args) throws Exception;

}
