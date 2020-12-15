package io.kimmking.rpcfx.server;

import io.kimmking.rpcfx.api.RpcfxException;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.kimmking.rpcfx.util.RpcfxContext;
import io.kimmking.rpcfx.util.ServiceUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.reflect.FastClass;

import java.util.concurrent.ThreadPoolExecutor;

public class RpcServerHandler extends SimpleChannelInboundHandler<RpcfxRequest> {

    private static final Logger logger = LoggerFactory.getLogger(RpcServerHandler.class);

    private final ThreadPoolExecutor serverHandlerPool;

    public RpcServerHandler(final ThreadPoolExecutor threadPoolExecutor) {
        this.serverHandlerPool = threadPoolExecutor;
    }

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, final RpcfxRequest request) {
        // filter beat ping
        if ("BEAT_PING_PONG".equalsIgnoreCase(request.getRequestId())) {
            logger.info("Server read heartbeat ping");
            return;
        }

        serverHandlerPool.execute(new Runnable() {
            @Override
            public void run() {
                logger.info("Receive request " + request.getRequestId());
                RpcfxResponse response = new RpcfxResponse();

                try {
                    Object result = handle(request);
                    response.setResult(result);
                    response.setRequestId(request.getRequestId());
                } catch (Throwable t) {
                    logger.error("RPC Server handle request error", t);
                    throw new RpcfxException(1000, "invoke error!");
                }
                ctx.writeAndFlush(response).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        logger.info("Send response for request " + request.getRequestId());
                    }
                });
            }
        });
    }

    private Object handle(RpcfxRequest request) throws Throwable {
        String className = request.getServiceClass();
        String version = request.getVersion();
        String serviceKey = ServiceUtil.makeServiceKey(className, version);
        Object serviceBean = RpcfxContext.getServiceBean(serviceKey);
        if (serviceBean == null) {
            logger.error("Can not find service implement with interface name: {} and version: {}", className, version);
            return null;
        }

        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethod();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParams();

        // JDK reflect
//        Method method = serviceClass.getMethod(methodName, parameterTypes);
//        method.setAccessible(true);
//        return method.invoke(serviceBean, parameters);

        // Cglib reflect
        FastClass serviceFastClass = FastClass.create(serviceClass);
//        FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
//        return serviceFastMethod.invoke(serviceBean, parameters);
        // for higher-performance
        int methodIndex = serviceFastClass.getIndex(methodName, parameterTypes);
        Object result = serviceFastClass.invoke(methodIndex, serviceBean, parameters);

//        RpcfxResponse response = new RpcfxResponse();
//        response.setResult(result);
//        response.setStatus(true);
        return result;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.warn("Server caught exception: " + cause.getMessage());
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            ctx.channel().close();
            logger.warn("Channel idle in last 15 seconds, close it...");
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

}
