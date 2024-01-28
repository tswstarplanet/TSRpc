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
import java.util.concurrent.ConcurrentHashMap;

public class HttpClientHandler extends SimpleChannelInboundHandler<FullHttpResponse> {

    private Manager manager;

    private static final Logger logger = LoggerFactory.getLogger(HttpClientHandler.class);

    private static final Map<String, ServiceResponse> serviceResponseMap = new ConcurrentHashMap<>();

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
        serviceResponseMap.put(serviceResponse.getRequestId(), serviceResponse);
    }

    public static ServiceResponse getServiceResponse(String requestId) {
        return serviceResponseMap.get(requestId);
    }

    public static void removeServiceResponse(String requestId) {
        serviceResponseMap.remove(requestId);
    }
}
