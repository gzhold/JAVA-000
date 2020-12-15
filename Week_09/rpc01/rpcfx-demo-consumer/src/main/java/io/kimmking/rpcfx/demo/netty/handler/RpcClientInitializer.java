package io.kimmking.rpcfx.demo.netty.handler;

import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.kimmking.rpcfx.netty.RpcfxDecoder;
import io.kimmking.rpcfx.netty.RpcfxEncoder;
import io.kimmking.rpcfx.serializer.KryoSerializer;
import io.kimmking.rpcfx.serializer.Serializer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class RpcClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        Serializer serializer = KryoSerializer.class.newInstance();
        ChannelPipeline cp = socketChannel.pipeline();
        cp.addLast(new IdleStateHandler(0, 0, 15, TimeUnit.SECONDS));
        cp.addLast(new RpcfxEncoder(RpcfxRequest.class, serializer));
        cp.addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0));
        cp.addLast(new RpcfxDecoder(RpcfxResponse.class, serializer));
        cp.addLast(new RpcClientHandler());
    }
}
