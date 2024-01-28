package com.wts.tsrpc.common;

public enum ServiceResponseCode {
    SUCCESS("000000", "成功"),
    INVALID_HTTP_METHOD("000001", "非法的Http方法"),
    HTTP_NOT_SUCCEED("000002", "Http状态不成功");
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
