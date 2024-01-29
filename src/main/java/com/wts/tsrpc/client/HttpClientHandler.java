package com.wts.tsrpc.client;

import com.wts.tsrpc.common.HttpUtils;
import com.wts.tsrpc.common.ServiceResponse;
import com.wts.tsrpc.exception.BizException;
import com.wts.tsrpc.server.manage.Manager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class HttpClientHandler extends SimpleChannelInboundHandler<FullHttpResponse> {

    private Manager manager;

    private static final Logger logger = LoggerFactory.getLogger(HttpClientHandler.class);

//    private static final Map<String, ServiceResponse> serviceResponseMap = new ConcurrentHashMap<>();

    private static final Map<String, BlockingQueue<ServiceResponse>> serviceResponseMap = new ConcurrentHashMap<>();

    public HttpClientHandler manager(Manager manager) {
        this.manager = manager;
        return this;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {
        if (HttpResponseStatus.OK.code() != msg.status().code()) {
            throw new BizException("Http respond not succeed !");
        }
        String body = HttpUtils.getBody(msg);
        ServiceResponse serviceResponse = manager.getTransform(msg.headers().get("transformType")).transformResponse(body);
        serviceResponseMap.putIfAbsent(serviceResponse.getRequestId(), new ArrayBlockingQueue<>(1));
        serviceResponseMap.get(serviceResponse.getRequestId()).put(serviceResponse);
    }

    public static ServiceResponse getServiceResponse(String requestId) {
        try {
            serviceResponseMap.putIfAbsent(requestId, new ArrayBlockingQueue<>(1));
            return serviceResponseMap.get(requestId).take();
        } catch (InterruptedException e) {
            throw new BizException("Store the service response error !");
        }
    }

    public static void removeServiceResponse(String requestId) {
        serviceResponseMap.get(requestId).clear();
        serviceResponseMap.remove(requestId);
    }
}
