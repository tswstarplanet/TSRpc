package com.wts.tsrpc.client;

import com.wts.tsrpc.server.manage.Manager;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;

public class HttpClientInitializer extends ChannelInitializer {

    private Manager manager;

    public HttpClientInitializer manager(Manager manager) {
        this.manager = manager;
        return this;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("Http-Client-Encoder&Decoder", new HttpClientCodec())
                .addLast("Http-Client-Aggregator", new HttpObjectAggregator(1024 * 1024))
                .addLast("Http-Client-Handler", (new HttpClientHandler()).manager(manager));
    }
}
