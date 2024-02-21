package com.wts.tsrpc.server.service;

import com.wts.tsrpc.client.service.ClientService;
import com.wts.tsrpc.common.ServiceRequest;
import com.wts.tsrpc.common.ServiceResponse;

import java.lang.reflect.Type;

public interface Transformer {
    ServiceRequest transformRequest(String body);

    ServiceResponse transformResponse(String body);

    Object transformReturnValueObject(String body, Type type);

    ServiceRequest transformRequest(ClientService clientService, Object[] arguments);

    String transformRequestToString(ServiceRequest request);

    String transformObjectToString(Object object);
}
