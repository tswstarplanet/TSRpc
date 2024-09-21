package com.wts.tsrpc.server;

import com.wts.tsrpc.common.transform.Transformers;
import com.wts.tsrpc.server.manage.ServerDispatcher;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    private ServerDispatcher serverDispatcher;

    private Transformers transformers;

    public HttpServerInitializer serverDispatcher(ServerDispatcher serverDispatcher) {
        this.serverDispatcher = serverDispatcher;
        return this;
    }

    public HttpServerInitializer transformers(Transformers transformers) {
        this.transformers = transformers;
        return this;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        var pipeline = ch.pipeline();

        pipeline.addLast("Http-Server-Encoder&Decoder", new HttpServerCodec())
                .addLast("Http-Server-Aggregator", new HttpObjectAggregator(1024 * 1024))
                .addLast("Http-Server-Handler", (new HttpServerHandler())
                        .serverDispatcher(serverDispatcher)
                        .transformers(transformers));
    }
}
