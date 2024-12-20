package com.wts.tsrpc.common;

import java.time.LocalDateTime;

public class ServiceRequest {

    private String applicationId;

    private String requestId;

    private String serviceId;

    private String methodName;

    private String[] argTypeNames;

    private String[] paramValueStrings;

    private Object[] paramValues;

    private LocalDateTime requestTime;

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String[] getParamValueStrings() {
        return paramValueStrings;
    }

    public void setParamValueStrings(String[] paramValueStrings) {
        this.paramValueStrings = paramValueStrings;
    }

    public Object[] getParamValues() {
        return paramValues;
    }

    public void setParamValues(Object[] paramValues) {
        this.paramValues = paramValues;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String[] getArgTypeNames() {
        return argTypeNames;
    }

    public void setArgTypeNames(String[] argTypeNames) {
        this.argTypeNames = argTypeNames;
    }
}
