package com.wts.tsrpc.common.transform;

import com.wts.tsrpc.client.ClientDispatcher;
import com.wts.tsrpc.client.service.ClientService;
import com.wts.tsrpc.common.ServiceRequest;
import com.wts.tsrpc.common.ServiceResponse;
import com.wts.tsrpc.server.manage.ServerDispatcher;
import com.wts.tsrpc.server.service.Transformer;

import java.lang.reflect.Type;

public class AbstractTransform implements Transformer {
    private ServerDispatcher serverDispatcher;

    private ClientDispatcher clientDispatcher;

    public AbstractTransform() {
    }

    public AbstractTransform(ServerDispatcher serverDispatcher, ClientDispatcher clientDispatcher) {
        this.serverDispatcher = serverDispatcher;
        this.clientDispatcher = clientDispatcher;
    }

    public Transformer serverDispatcher(ServerDispatcher serverDispatcher) {
        this.serverDispatcher = serverDispatcher;
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

    public ServerDispatcher getServerDispatcher() {
        return serverDispatcher;
    }

    public ClientDispatcher getClientDispatcher() {
        return clientDispatcher;
    }
}
