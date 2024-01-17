package com.wts.tsrpc.server;

import com.wts.tsrpc.manage.Manager;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    private Manager manager;

    public HttpServerInitializer manager(Manager manager) {
        this.manager = manager;
        return this;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        var pipeline = ch.pipeline();

        pipeline.addLast("Http-Encoder&Decoder", new HttpServerCodec())
                .addLast("Http-Aggregator", new HttpObjectAggregator(1024 * 1024))
                .addLast("Http-Server-Handler", (new HttpServerHandler())
                        .manager(manager));
    }
}
