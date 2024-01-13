package com.wts.tsrpc.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("Http-Encoder&Decoder", new HttpServerCodec())
                .addLast("Http-Aggregator", new HttpObjectAggregator(1024 * 1024))
                .addLast("Http-Server-Handler", new HttpServerHandler());
    }
}
