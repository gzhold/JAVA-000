package io.github.gateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;


import java.net.InetSocketAddress;

public class BlackWhiteListFilter implements HttpRequestFilter {

    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        // 本地测试
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel()
                    .remoteAddress();
        String clientIP = insocket.getAddress().getHostAddress();

        // 校验ip

        System.out.println("ip address is  = " + clientIP);
    }
}
