package io.kimmking.rpcfx.api;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.Objects;

public class RpcfxServiceInfo implements Serializable {

    // interface name
    private String serviceName;
    // version
    private String version;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RpcfxServiceInfo that = (RpcfxServiceInfo) o;
        return Objects.equals(serviceName, that.serviceName) &&
                    Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceName, version);
    }

    public String toJson() {
        return JSON.toJSONString(this);
    }

    @Override
    public String toString() {
        return toJson();
    }

}
