package com.wts.tsrpc.server;

import com.wts.tsrpc.common.Transformer;
import com.wts.tsrpc.server.concurrent.ServerThreadPool;
import com.wts.tsrpc.server.manage.ServiceDispatcher;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    private ServiceDispatcher serviceDispatcher;

    private Transformer transformer;

    private ServerThreadPool serverThreadPool;

    public HttpServerInitializer serviceDispatcher(ServiceDispatcher serviceDispatcher) {
        this.serviceDispatcher = serviceDispatcher;
        return this;
    }

    public HttpServerInitializer transformer(Transformer transformers) {
        this.transformer = transformers;
        return this;
    }

    public HttpServerInitializer serverThreadPool(ServerThreadPool serverThreadPool) {
        this.serverThreadPool = serverThreadPool;
        return this;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        var pipeline = ch.pipeline();

        pipeline.addLast("Http-Server-Encoder&Decoder", new HttpServerCodec())
                .addLast("Http-Server-Aggregator", new HttpObjectAggregator(1024 * 1024))
                .addLast("Http-Server-Handler", (new HttpServerHandler())
                        .serverDispatcher(serviceDispatcher)
                        .transformer(transformer)
                        .executorService(serverThreadPool.getExecutorService()));
    }
}
