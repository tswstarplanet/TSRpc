package com.wts.tsrpc.server.service;

public class ServiceResponse {
    private String code;

    private String msg;

    private Object body;

    public ServiceResponse() {
    }

    public ServiceResponse(String code, String msg, Object body) {
        this.code = code;
        this.msg = msg;
        this.body = body;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}
