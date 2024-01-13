package com.wts.tsrpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class HttpServer {

    private Integer port = 6688;

    private Integer bossNum = 1;

    private Integer workerNum = 10;

    public HttpServer() {}

    public HttpServer(Integer port, Integer bossNum, Integer workerNum) {
        this.port = port;
        this.bossNum = bossNum;
        this.workerNum = workerNum;
    }

    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(bossNum);
        EventLoopGroup workerGroup = new NioEventLoopGroup(workerNum);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new HttpServerInitializer());
            ChannelFuture future = bootstrap.bind(port).sync();
            System.out.println("Server start up on port: " + port);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            System.out.println("Server run error");
            e.printStackTrace();
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
}
