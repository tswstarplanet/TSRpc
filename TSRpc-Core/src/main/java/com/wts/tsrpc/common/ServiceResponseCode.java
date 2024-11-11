package com.wts.tsrpc.common;

public enum ServiceResponseCode {
    SUCCESS("000000", "success"),
    INVALID_HTTP_METHOD("000001", "invalid http method"),
    HTTP_NOT_SUCCEED("000002", "http not succeed"),;
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
