package io.kimmking.rpcfx.api;

public class RpcfxException extends RuntimeException {

    private int errorCode;

    public RpcfxException () {
        super();
    }

    public RpcfxException (int errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public RpcfxException(String message) {
        super(message);
    }

    public RpcfxException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    // ...
}
