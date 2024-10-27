package com.wts.tsrpc.common.transform;

import com.wts.tsrpc.client.ClientDispatcher;
import com.wts.tsrpc.client.service.ClientService;
import com.wts.tsrpc.common.ServiceRequest;
import com.wts.tsrpc.common.ServiceResponse;
import com.wts.tsrpc.server.manage.ServiceDispatcher;
import com.wts.tsrpc.common.Transformer;

import java.lang.reflect.Type;

public class AbstractTransform implements Transformer {
    private ServiceDispatcher serviceDispatcher;

    private ClientDispatcher clientDispatcher;

    public AbstractTransform() {
    }

    public AbstractTransform(ServiceDispatcher serviceDispatcher, ClientDispatcher clientDispatcher) {
        this.serviceDispatcher = serviceDispatcher;
        this.clientDispatcher = clientDispatcher;
    }

    public Transformer serverDispatcher(ServiceDispatcher serviceDispatcher) {
        this.serviceDispatcher = serviceDispatcher;
        return this;
    }

    public Transformer clientDispatcher(ClientDispatcher clientDispatcher) {
        this.clientDispatcher = clientDispatcher;
        return this;
    }

    @Override
    public ServiceRequest transformRequest(String body) {
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

    @Override
    public ServiceRequest transformRequest(ClientService clientService, Object[] arguments) {
        return null;
    }

    public ServiceDispatcher getServerDispatcher() {
        return serviceDispatcher;
    }

    public ClientDispatcher getClientDispatcher() {
        return clientDispatcher;
    }
}
