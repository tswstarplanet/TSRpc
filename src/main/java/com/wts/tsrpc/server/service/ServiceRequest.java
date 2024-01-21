package com.wts.tsrpc.server.service;

public class ServiceRequest {
    private String serviceId;

    private String[] paramValueStrings;

    private Object[] paramValues;

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
}
