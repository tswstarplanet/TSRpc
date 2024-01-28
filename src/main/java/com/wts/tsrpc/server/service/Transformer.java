package com.wts.tsrpc.server.service;

import com.wts.tsrpc.client.ClientService;
import com.wts.tsrpc.common.ServiceRequest;
import com.wts.tsrpc.common.ServiceResponse;

public interface Transformer {
    ServiceRequest transformRequest(String body);

    ServiceResponse transformResponse(String body);

    ServiceRequest transformRequest(ClientService clientService, Object[] arguments);

    String transformRequestToString(ServiceRequest request);
}
