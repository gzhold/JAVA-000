package io.kimmking.rpcfx.demo.netty.server;

import io.kimmking.rpcfx.demo.registry.ServiceRegistry;
import io.kimmking.rpcfx.util.ThreadPoolUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadPoolExecutor;

public class NettyServer extends Server {
    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private final int processors = Runtime.getRuntime().availableProcessors();

    ThreadPoolExecutor threadPoolExecutor = ThreadPoolUtil.buildThreadPool(
                NettyServer.class.getSimpleName(), processors, processors * 2);

    private String serverAddress;
    private ServiceRegistry serviceRegistry;

    public NettyServer(String serverAddress, String registryAddress) {
        this.serverAddress = serverAddress;
        this.serviceRegistry = new ServiceRegistry(registryAddress);
    }


    /**
     * start server
     *
     * @throws Exception
     */
    @Override
    public void start() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                        .childHandler(new RpcServerInitializer(threadPoolExecutor))
                        .option(ChannelOption.SO_BACKLOG, 128)
                        .childOption(ChannelOption.SO_KEEPALIVE, true);

            String[] array = serverAddress.split(":");
            String host = array[0];
            int port = Integer.parseInt(array[1]);
            ChannelFuture future = bootstrap.bind(host, port).sync();

            if (serviceRegistry != null) {
                serviceRegistry.registerService(host, port);
            }
            logger.info("Server started on port {}", port);
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            if (e instanceof InterruptedException) {
                logger.info("Rpc server remoting server stop");
            } else {
                logger.error("Rpc server remoting server error", e);
            }
        } finally {
            try {
                serviceRegistry.unregisterService();
                workerGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }

    /**
     * stop server
     *
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {

    }
}
