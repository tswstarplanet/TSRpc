package com.wts.tsrpc.server;

import com.wts.tsrpc.manage.Manager;
import com.wts.tsrpc.service.ServiceRequest;
import com.wts.tsrpc.service.ServiceResponse;
import com.wts.tsrpc.service.ServiceResponseCode;
import com.wts.tsrpc.utils.Checker;
import com.wts.tsrpc.utils.GsonUtils;
import com.wts.tsrpc.utils.HttpContentType;
import com.wts.tsrpc.utils.ResponseUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
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
            FullHttpResponse httpResponse = ResponseUtils.buildCommonHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.METHOD_NOT_ALLOWED, HttpContentType.APPLICATION_JSON.getContentType(), response);
            ctx.writeAndFlush(httpResponse).addListener(ChannelFutureListener.CLOSE);
            return;
        }

        try {
            logger.info("method: {}", request.method().name());
            logger.info("uri: {}", request.uri());
            logger.info("http version: " + request.protocolVersion().text());
            logger.info("head: " + request.headers().toString());
            logger.info("request body: " + getBody(request));

            String body = getBody(request);

            ServiceRequest serviceRequest = manager.getTransform(request.headers().get("transformType")).transform(body);

            ServiceResponse serviceResponse = manager.getDispatcher(request.headers().get("dispatcherType")).dispatch(serviceRequest);

            logger.info("channel = " + ctx.channel() + ", pipeline = " + ctx.pipeline() + ", channel of pipeline: " + ctx.pipeline().channel());
            logger.info("current handler = " + ctx.handler());

            logger.info("ctx type = " + ctx.getClass());
            logger.info("pipeline hashcode = " + ctx.pipeline().hashCode() + ", HttpServerHandler hash = " + this.hashCode());
            logger.info("msg type = " + request.getClass());
            logger.info("client addr = " + ctx.channel().remoteAddress());

            URI uri = new URI(request.uri());
            if ("/favicon.ico".equals(uri.getPath())) {
                logger.error("Request favicon.icn, do not response");
                return;
            }

            FullHttpResponse response = ResponseUtils.buildCommonHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, HttpContentType.APPLICATION_JSON.getContentType(), serviceResponse);

            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        } catch (Exception e) {
            logger.error("Exception when handle request !", e);
//            ByteBuf content = Unpooled.copiedBuffer("Exception when handle request !", CharsetUtil.UTF_8);
            FullHttpResponse response = ResponseUtils.buildCommonHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, HttpContentType.APPLICATION_JSON.getContentType(), "Exception when handle request: " + e.getMessage());

            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }


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

    public static void main(String[] args) {
//        List<String> array = GsonUtils.parseObject("[\"abc\",\"def\"]", List.class);
//        for (String ele : array) {
//            System.out.println(ele);
//        }
        System.out.println(GsonUtils.parseObject("[\"abc\",\"def\"]", Object[].class));
        ServiceRequest request = new ServiceRequest();
        request.setServiceId("primitiveService");
        request.setParamValueStrings(new String[] {"abc", "12"});
        System.out.println(GsonUtils.toJsonString(request));
    }

}
