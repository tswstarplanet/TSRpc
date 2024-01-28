package com.wts.tsrpc.common;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpMessage;

import java.nio.charset.StandardCharsets;

public class HttpUtils {
    public static String getBody(FullHttpMessage request) {
        ByteBuf buf = request.content();
        return buf.toString(StandardCharsets.UTF_8);
    }

}
