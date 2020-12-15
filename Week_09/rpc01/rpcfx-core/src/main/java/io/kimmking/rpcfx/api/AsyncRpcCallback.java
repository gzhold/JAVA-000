package io.kimmking.rpcfx.api;

public interface AsyncRpcCallback {

    void success(Object result);

    void fail(Exception e);

}
