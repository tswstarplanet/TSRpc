package com.wts.tsrpc.service;

public enum ServiceResponseCode {
    SUCCESS("000000", "成功"),
    INVALID_HTTP_METHOD("000001", "非法的Http方法");
    private final String code;

    private final String msg;

    ServiceResponseCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
