package com.wts.tsrpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpServer {

    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);

    private Integer port = 6688;

    private Integer bossNum = 1;

    private Integer workerNum = 10;

    private ChannelHandler serverInitializer;

    public HttpServer() {}

    public HttpServer(Integer port, Integer bossNum, Integer workerNum) {
        this.port = port;
        this.bossNum = bossNum;
        this.workerNum = workerNum;
    }

    public HttpServer port(Integer port) {
        this.port = port;
        return this;
    }

    public HttpServer bossNum(Integer bossNum) {
        this.bossNum = bossNum;
        return this;
    }

    public HttpServer workerNum(Integer workerNum) {
        this.workerNum = workerNum;
        return this;
    }

    public HttpServer serverInitializer(ChannelHandler serverInitializer) {
        this.serverInitializer = serverInitializer;
        return this;
    }

    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(bossNum);
        EventLoopGroup workerGroup = new NioEventLoopGroup(workerNum);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .channel(NioServerSocketChannel.class)
                    .childHandler(serverInitializer);
            ChannelFuture future = bootstrap.bind(port).sync();
            logger.info("Server start up on port: " + port);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("Server run error: ", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getBossNum() {
        return bossNum;
    }

    public void setBossNum(Integer bossNum) {
        this.bossNum = bossNum;
    }

    public Integer getWorkerNum() {
        return workerNum;
    }

    public void setWorkerNum(Integer workerNum) {
        this.workerNum = workerNum;
    }

    public ChannelHandler getServerInitializer() {
        return serverInitializer;
    }

    public void setServerInitializer(ChannelHandler serverInitializer) {
        this.serverInitializer = serverInitializer;
    }

}
