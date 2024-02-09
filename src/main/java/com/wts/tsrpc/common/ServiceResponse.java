package com.wts.tsrpc.common;

import java.util.List;

public class ServiceResponse {

    private String applicationId;

    private String requestId;

    private String serviceId;

    private String code;

    private String msg;

    private Object body;

    private String returnValueString;

    private Object returnValue;

    private List<String> methodParamTypeNames;

    private String methodName;

    public ServiceResponse() {
    }

    public ServiceResponse(String code, String msg, String body) {
        this.code = code;
        this.msg = msg;
        this.body = body;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
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

    public List<String> getMethodParamTypeNames() {
        return methodParamTypeNames;
    }

    public void setMethodParamTypeNames(List<String> methodParamTypeNames) {
        this.methodParamTypeNames = methodParamTypeNames;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
