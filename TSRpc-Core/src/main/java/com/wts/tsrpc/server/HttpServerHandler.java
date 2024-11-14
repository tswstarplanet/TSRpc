package com.wts.tsrpc.server;

import com.wts.tsrpc.common.HttpUtils;
import com.wts.tsrpc.common.ServiceRequest;
import com.wts.tsrpc.common.ServiceResponse;
import com.wts.tsrpc.common.ServiceResponseCode;
import com.wts.tsrpc.common.Transformer;
import com.wts.tsrpc.common.utils.Checker;
import com.wts.tsrpc.common.utils.GsonUtils;
import com.wts.tsrpc.common.utils.HttpContentType;
import com.wts.tsrpc.common.utils.ResponseUtils;
import com.wts.tsrpc.server.manage.ServiceDispatcher;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final Logger logger = LoggerFactory.getLogger(HttpServerHandler.class);

    private Transformer transformer;

    private ServiceDispatcher serviceDispatcher;

    private ExecutorService executorService;

    public HttpServerHandler() {

    }

    public HttpServerHandler transformer(Transformer transformer) {
        this.transformer = transformer;
        return this;
    }

    public HttpServerHandler serverDispatcher(ServiceDispatcher serviceDispatcher) {
        this.serviceDispatcher = serviceDispatcher;
        return this;
    }

    public HttpServerHandler executorService(ExecutorService executorService) {
        this.executorService = executorService;
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

        ServiceRequest serviceRequest = null;

        try {
//            logger.info("method: {}", request.method().name());
//            logger.info("uri: {}", request.uri());
//            logger.info("http version: " + request.protocolVersion().text());
//            logger.info("head: " + request.headers().toString());
//            logger.info("request body: " + HttpUtils.getBody(request));

//            URI uri = new URI(request.uri());
//            if ("/favicon.ico".equals(uri.getPath())) {
//                logger.error("Request favicon.icn, do not response");
//                return;
//            }
            logger.info("method: {}", request.method().name());
            logger.info("uri: {}", request.uri());
            logger.info("http version: " + request.protocolVersion().text());
            logger.info("head: " + request.headers().toString());
            logger.info("request body: " + HttpUtils.getBody(request));
            logger.info("channel = " + ctx.channel() + ", pipeline = " + ctx.pipeline() + ", channel of pipeline: " + ctx.pipeline().channel());
            logger.info("current handler = " + ctx.handler());

            logger.info("ctx type = " + ctx.getClass());
            logger.info("pipeline hashcode = " + ctx.pipeline().hashCode() + ", HttpServerHandler hash = " + this.hashCode());
            logger.info("msg type = " + request.getClass());
            logger.info("client addr = " + ctx.channel().remoteAddress());
            String body = HttpUtils.getBody(request);

            serviceRequest = transformer.transformRequest(body);

            ServiceRequest finalServiceRequest = serviceRequest;
            CompletableFuture<ServiceResponse> future = CompletableFuture.supplyAsync(() -> serviceDispatcher.dispatch(finalServiceRequest), executorService)
                    .orTimeout(60000, TimeUnit.MILLISECONDS)
                    .whenComplete((serviceResponse, ex) -> {
                        if (ex != null) {
                            if (ex instanceof TimeoutException || ex.getCause() instanceof TimeoutException) {
                                logger.error("Exception when handle request !", ex);
                                ServiceResponse errorServiceResponse = ResponseUtils.buildServiceResponse(ServiceResponseCode.SERVER_EXECUTE_ERROR.getCode(), ServiceResponseCode.SERVER_EXECUTE_ERROR.getMsg() + ": " + ex.getMessage());
                                errorServiceResponse.setRequestId(finalServiceRequest.getRequestId());
                                FullHttpResponse response = ResponseUtils.buildCommonHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, HttpContentType.APPLICATION_JSON.getContentType(), errorServiceResponse);
                                ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
                            } else {
                                logger.error("Exception when handle request !", ex);
                                ServiceResponse errorServiceResponse = ResponseUtils.buildServiceResponse(ServiceResponseCode.SERVER_EXECUTE_ERROR.getCode(), ServiceResponseCode.SERVER_EXECUTE_ERROR.getMsg() + ": " + ex.getMessage());
                                errorServiceResponse.setRequestId(finalServiceRequest.getRequestId());
                                FullHttpResponse response = ResponseUtils.buildCommonHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, HttpContentType.APPLICATION_JSON.getContentType(), errorServiceResponse);
                                ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
                            }
                        } else {
                            logger.debug("Service of requestId = " + serviceResponse.getRequestId() + " execute success !");
                            FullHttpResponse response = ResponseUtils.buildCommonHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, HttpContentType.APPLICATION_JSON.getContentType(), serviceResponse);
                            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
                        }})
//                    .exceptionally(throwable -> {
//                        logger.error("Exception when handle request !", throwable);
//                        ServiceResponse serviceResponse = ResponseUtils.buildServiceResponse(ServiceResponseCode.SERVER_EXECUTE_ERROR.getCode(), ServiceResponseCode.SERVER_EXECUTE_ERROR.getMsg() + ": " + throwable.getMessage());
//                        serviceResponse.setRequestId(finalServiceRequest.getRequestId());
//                        FullHttpResponse response = ResponseUtils.buildCommonHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, HttpContentType.APPLICATION_JSON.getContentType(), serviceResponse);
//                        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
//                        return null;
//                    })
                    ;
//            future.get();
//            ServiceResponse serviceResponse = serviceDispatcher.dispatch(serviceRequest);

//            logger.info("channel = " + ctx.channel() + ", pipeline = " + ctx.pipeline() + ", channel of pipeline: " + ctx.pipeline().channel());
//            logger.info("current handler = " + ctx.handler());
//
//            logger.info("ctx type = " + ctx.getClass());
//            logger.info("pipeline hashcode = " + ctx.pipeline().hashCode() + ", HttpServerHandler hash = " + this.hashCode());
//            logger.info("msg type = " + request.getClass());
//            logger.info("client addr = " + ctx.channel().remoteAddress());



//            FullHttpResponse response = ResponseUtils.buildCommonHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, HttpContentType.APPLICATION_JSON.getContentType(), serviceResponse);

//            response.headers().set("transformType", "jackson");

//            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        } catch (Exception e) {
            logger.error("Exception when handle request !", e);
//            ByteBuf content = Unpooled.copiedBuffer("Exception when handle request !", CharsetUtil.UTF_8);
            ServiceResponse serviceResponse = ResponseUtils.buildServiceResponse(ServiceResponseCode.SERVER_EXECUTE_ERROR.getCode(), ServiceResponseCode.SERVER_EXECUTE_ERROR.getMsg() + ": " + e.getMessage());
            if (serviceRequest != null) {
                serviceResponse.setRequestId(serviceRequest.getRequestId());
            }
            FullHttpResponse response = ResponseUtils.buildCommonHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, HttpContentType.APPLICATION_JSON.getContentType(), serviceResponse);

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
