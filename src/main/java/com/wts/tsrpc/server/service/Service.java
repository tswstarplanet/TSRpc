package com.wts.tsrpc.server.service;

import java.util.ArrayList;
import java.util.List;

public class Service {

    private String serviceId;

    private String classFullName;

    private List<ServiceMethod> methods = new ArrayList<>();

    public Service serviceId(String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    public Service classFullName(String classFullName) {
        this.classFullName = classFullName;
        return this;
    }

    public Service method(ServiceMethod method) {
        methods.add(method);
        return this;
    }

    public String getClassFullName() {
        return classFullName;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public void setClassFullName(String classFullName) {
        this.classFullName = classFullName;
    }

    public List<ServiceMethod> getMethods() {
        return methods;
    }

    public void setMethods(List<ServiceMethod> methods) {
        this.methods = methods;
    }
}
