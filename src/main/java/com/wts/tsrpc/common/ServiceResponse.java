package com.wts.tsrpc.common;

public class ServiceResponse {

    private String requestId;

    private String serviceId;

    private String code;

    private String msg;

    private Object body;

    private String returnValueString;

    private Object returnValue;

    public ServiceResponse() {
    }

    public ServiceResponse(String code, String msg, String body) {
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

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }

    public String getReturnValueString() {
        return returnValueString;
    }

    public void setReturnValueString(String returnValueString) {
        this.returnValueString = returnValueString;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
