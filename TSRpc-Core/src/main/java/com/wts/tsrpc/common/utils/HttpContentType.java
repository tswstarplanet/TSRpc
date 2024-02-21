package com.wts.tsrpc.common.utils;

public class HttpContentType {
    public static final HttpContentType APPLICATION_JSON = new HttpContentType("application/json; charset=UTF-8");

    private final String contentType;

    private HttpContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }
}
