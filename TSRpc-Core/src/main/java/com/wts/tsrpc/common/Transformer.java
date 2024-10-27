package com.wts.tsrpc.common;

import com.wts.tsrpc.client.service.ClientService;

import java.lang.reflect.Type;

/**
 * Transform between message and request or response.
 */
public interface Transformer {

    /**
     * Transform request body to framework request object.
     */
    ServiceRequest transformRequest(String body);

    /**
     * Transform response body to framework response object.
     */
    ServiceResponse transformResponse(String body);

    /**
     * Transform response body to object according to generic type.
     */
    Object transformReturnValueObject(String body, Type type);

    /**
     * Transform and wrapper rpc method arguments to framework request object.
     */
    ServiceRequest transformRequest(ClientService clientService, Object[] arguments);

    /**
     * Transform framework object to string.
     */
    String transformRequestToString(ServiceRequest request);

    /**
     * Transform object to string.
     */
    String transformObjectToString(Object object);
}
