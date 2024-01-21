package com.wts.tsrpc.utils;

import com.wts.tsrpc.server.service.ServiceResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

public class ResponseUtils {
    public static ServiceResponse buildServiceResponse(String code, String msg) {
        ServiceResponse response = new ServiceResponse();
        response.setCode(code);
        response.setMsg(msg);
        return response;
    }

    public static FullHttpResponse buildCommonHttpResponse(HttpVersion httpVersion, HttpResponseStatus status, String contentType, Object object) {
        ByteBuf content = Unpooled.copiedBuffer(GsonUtils.toJsonString(object), CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(httpVersion, status, content);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, contentType);
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
        return response;
    }
}
