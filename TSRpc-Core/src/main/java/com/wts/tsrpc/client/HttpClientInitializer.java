package com.wts.tsrpc.client;

import com.wts.tsrpc.common.transform.Transformers;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;

public class HttpClientInitializer extends ChannelInitializer {

    private Transformers transformers;

    public HttpClientInitializer transformers(Transformers transformers) {
        this.transformers = transformers;
        return this;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("Http-Client-Encoder&Decoder", new HttpClientCodec())
                .addLast("Http-Client-Aggregator", new HttpObjectAggregator(1024 * 1024))
                .addLast("Http-Client-Handler", (new HttpClientHandler()).transformers(transformers));
    }
}
