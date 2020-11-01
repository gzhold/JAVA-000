package io.github.gateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccessLogFilter implements HttpRequestFilter {

    private static Logger logger = LoggerFactory.getLogger(AccessLogFilter.class);

    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        System.out.println("AccessLogFilter pre, headers is {}" + fullRequest.headers());
        fullRequest.headers().add("nio", "gzhold");
//        logger.info("AccessLogFilter, name is {}", fullRequest.getClass().getCanonicalName());

        System.out.println("AccessLogFilter post, headers is {}" + fullRequest.headers());
    }
}
