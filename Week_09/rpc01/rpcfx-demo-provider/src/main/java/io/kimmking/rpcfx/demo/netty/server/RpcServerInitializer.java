package io.kimmking.rpcfx.demo.netty.server;

import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.kimmking.rpcfx.netty.RpcfxDecoder;
import io.kimmking.rpcfx.netty.RpcfxEncoder;
import io.kimmking.rpcfx.serializer.KryoSerializer;
import io.kimmking.rpcfx.serializer.Serializer;
import io.kimmking.rpcfx.server.RpcServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;


import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RpcServerInitializer extends ChannelInitializer<SocketChannel> {

    private ThreadPoolExecutor threadPoolExecutor;

    public RpcServerInitializer(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    @Override
    public void initChannel(SocketChannel channel) throws Exception {
        Serializer serializer = KryoSerializer.class.newInstance();
        ChannelPipeline cp = channel.pipeline();
        cp.addLast(new IdleStateHandler(0, 0, 15, TimeUnit.SECONDS));
        cp.addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0));
        cp.addLast(new RpcfxDecoder(RpcfxRequest.class, serializer));
        cp.addLast(new RpcfxEncoder(RpcfxResponse.class, serializer));
        cp.addLast(new RpcServerHandler(threadPoolExecutor));
    }

}
