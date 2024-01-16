package com.wts.tsrpc.server;

import com.wts.tsrpc.manage.Manager;
import com.wts.tsrpc.service.ServiceRequest;
import com.wts.tsrpc.service.ServiceResponse;
import com.wts.tsrpc.service.ServiceResponseCode;
import com.wts.tsrpc.utils.Checker;
import com.wts.tsrpc.utils.HttpContentType;
import com.wts.tsrpc.utils.JsonUtils;
import com.wts.tsrpc.utils.ResponseUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Logger logger = LoggerFactory.getLogger(HttpServerHandler.class);

    private Manager manager;

    public HttpServerHandler() {

    }

    public HttpServerHandler manager(Manager manager) {
        this.manager = manager;
        return this;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        if (!Checker.HTTP_METHOD_CHECK.getPredicate().test(request.method().name())) {
            ServiceResponse response = ResponseUtils.buildServiceResponse(ServiceResponseCode.INVALID_HTTP_METHOD.getCode(), ServiceResponseCode.INVALID_HTTP_METHOD.getMsg());
//            ByteBuf content = Unpooled.copiedBuffer(JsonUtils.toJsonString(response), CharsetUtil.UTF_8);
            FullHttpResponse httpResponse = ResponseUtils.buildCommonHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.METHOD_NOT_ALLOWED, HttpContentType.APPLICATION_JSON.getContentType(), response);
            ctx.writeAndFlush(httpResponse).addListener(ChannelFutureListener.CLOSE);
            return;
        }

        logger.info("method: {}", request.method().name());
        logger.info("uri: {}", request.uri());
        logger.info("http version: " + request.protocolVersion().text());
        logger.info("head: " + request.headers().toString());
        logger.info("request body: " + getBody(request));


//        System.out.println("method: " + request.method().name());
//        System.out.println("uri: " + request.uri());
//        System.out.println("http version: " + request.protocolVersion().text());
//
//        System.out.println("head: " + request.headers().entries());
//
//        System.out.println("request body: " + getBody(request));

        String body = getBody(request);

        ServiceRequest serviceRequest = manager.getTransform(request.headers().get("transformType")).transform(body);

        ServiceResponse serviceResponse = manager.getDispatcher(request.headers().get("dispatcherType")).dispatch(serviceRequest);

//        System.out.println("channel = " + ctx.channel() + ", pipeline = " + ctx.pipeline() + ", channel of pipeline: " + ctx.pipeline().channel());
//        System.out.println("current handler = " + ctx.handler());
//
//        System.out.println("ctx type = " + ctx.getClass());
//        System.out.println("pipeline hashcode = " + ctx.pipeline().hashCode() + ", HttpServerHandler hash = " + this.hashCode());
//        System.out.println("msg type = " + request.getClass());
//        System.out.println("client addr = " + ctx.channel().remoteAddress());

        URI uri = new URI(request.uri());
        if ("/favicon.ico".equals(uri.getPath())) {
            System.out.println("Request favicon.icn, do not response");
            return;
        }
        ByteBuf content = Unpooled.copiedBuffer(JsonUtils.toJsonString(serviceResponse), CharsetUtil.UTF_8);
        FullHttpResponse response = ResponseUtils.buildCommonHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, HttpContentType.APPLICATION_JSON.getContentType(), content);

//        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
//        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
//        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private Object getXWWWFormRequestBody(FullHttpRequest request) {
        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), request);
        List<InterfaceHttpData> httpPostData = decoder.getBodyHttpDatas();
        Map<String, String> params = new HashMap<>();
        for (InterfaceHttpData data : httpPostData) {
            if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                MemoryAttribute attribute = (MemoryAttribute) data;
                params.put(attribute.getName(), attribute.getValue());
            }
        }
        return params;
    }

    private String getBody(FullHttpRequest request) {
        ByteBuf buf = request.content();
        return buf.toString(StandardCharsets.UTF_8);
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }
}
