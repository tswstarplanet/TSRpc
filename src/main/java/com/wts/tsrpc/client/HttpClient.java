package com.wts.tsrpc.client;

import com.wts.tsrpc.exception.BizException;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpClient {
    private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);

    private static final Map<Endpoint, HttpClient> httpClientMap = new ConcurrentHashMap<>();

    private final Integer workNum;

    private Bootstrap bootstrap;

    private ChannelFuture channelFuture;

    private String host;

    private Integer port;

    public HttpClient(String host, Integer port, Integer workNum) {
        this.host = host;
        this.port = port;
        this.workNum = workNum;
        bootstrap = (new Bootstrap())
                .group(new NioEventLoopGroup(workNum))
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new HttpClientInitializer());
    }

    public ChannelFuture connect() {
        this.channelFuture = bootstrap.connect(host, port);
        return channelFuture;
    }

    public ChannelFuture sendMsg(String msg) {
        FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, STR."http://\{host}:\{port}"
                , Unpooled.wrappedBuffer(msg.getBytes(StandardCharsets.UTF_8)));
        request.headers().set(HttpHeaderNames.HOST, host)
                .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
                .set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
        channelFuture.channel().write(request);
        channelFuture.channel().flush();
        try {
            return channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new BizException(STR."Send msg error: \{e.getMessage()}");
        }
    }

    public static HttpClient getHttpClient(Endpoint endpoint) {
        if (endpoint == null) {
            throw new BizException("Endpoint object is null !");
        }
        return httpClientMap.get(endpoint);
    }

    public static HttpClient addHttpClient(Endpoint endpoint, HttpClient httpClient) {
        if (endpoint == null) {
            throw new BizException("Endpoint object is null !");
        }
        return httpClientMap.computeIfAbsent(endpoint, _ -> httpClientMap.put(endpoint, httpClient));
    }
}
