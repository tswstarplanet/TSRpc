package com.wts.tsrpc.server.service;

import com.wts.tsrpc.client.ClientService;
import com.wts.tsrpc.common.ServiceRequest;
import com.wts.tsrpc.common.ServiceResponse;
import com.wts.tsrpc.server.manage.Manager;

import java.lang.reflect.Type;

public class AbstractTransform implements Transformer {
    private Manager manager;

    public Transformer manager(Manager manager) {
        this.manager = manager;
        return this;
    }

    @Override
    public ServiceRequest transformRequest(String body) {
        return null;
    }

    @Override
    public ServiceRequest transformRequest(ClientService clientService, Object[] arguments) {
        return null;
    }

    @Override
    public String transformRequestToString(ServiceRequest request) {
        return null;
    }

    @Override
    public ServiceResponse transformResponse(String body) {
        return null;
    }

    @Override
    public String transformObjectToString(Object object) {
        return null;
    }

    @Override
    public Object transformReturnValueObject(String body, Type type) {
        return null;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }
}
