package com.wts.tsrpc.server;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.wts.tsrpc.exception.BizException;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    private List<Pair<String, ChannelHandler>> channelHandlerPairs = new ArrayList<>();

    public HttpServerInitializer addChannelHandler(String name, ChannelHandler channelHandler) {
        if (StringUtils.isEmpty(name)) {
            throw new BizException("Name is null when add Channel Handler !");
        }
        if (channelHandler == null) {
            throw new BizException("Channel Handler is null when add Channel Handler");
        }
        channelHandlerPairs.add(new ImmutablePair<>(name, channelHandler));
        return this;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        if (channelHandlerPairs.isEmpty()) {
            throw new BizException("Channel Handler Pairs is empty !");
        }
        var pipeline = ch.pipeline();
        channelHandlerPairs.forEach(pair -> pipeline.addLast(pair.getLeft(), pair.getRight()));

//        pipeline.addLast("Http-Encoder&Decoder", new HttpServerCodec())
//                .addLast("Http-Aggregator", new HttpObjectAggregator(1024 * 1024))
//                .addLast("Http-Server-Handler", new HttpServerHandler());
    }
}
