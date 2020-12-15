package io.kimmking.rpcfx.protocol;

import com.alibaba.fastjson.JSON;
import io.kimmking.rpcfx.api.RpcfxServiceInfo;
import io.kimmking.rpcfx.util.JsonUtil;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class RpcfxProtocol implements Serializable {

    // service host
    private String host;
    // service port
    private int port;
    // service info list
    private List<RpcfxServiceInfo> serviceInfoList;

    public String toJson() {
        return JsonUtil.objectToJson(this);
    }

    public static RpcfxProtocol fromJson(String json) {
        return JsonUtil.jsonToObject(json, RpcfxProtocol.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RpcfxProtocol that = (RpcfxProtocol) o;
        return port == that.port &&
                    Objects.equals(host, that.host) &&
                    isListEquals(serviceInfoList, that.getServiceInfoList());
    }

    private boolean isListEquals(List<RpcfxServiceInfo> thisList, List<RpcfxServiceInfo> thatList) {
        if (thisList == null && thatList == null) {
            return true;
        }
        if ((thisList == null && thatList != null)
                    || (thisList != null && thatList == null)
                    || (thisList.size() != thatList.size())) {
            return false;
        }
        return thisList.containsAll(thatList) && thatList.containsAll(thisList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port, serviceInfoList.hashCode());
    }

    @Override
    public String toString() {
        return toJson();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public List<RpcfxServiceInfo> getServiceInfoList() {
        return serviceInfoList;
    }

    public void setServiceInfoList(List<RpcfxServiceInfo> serviceInfoList) {
        this.serviceInfoList = serviceInfoList;
    }
}
