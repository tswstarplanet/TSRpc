package com.wts.tsrpc.client;

import com.wts.tsrpc.common.ServiceRequest;
import com.wts.tsrpc.common.ServiceResponse;
import com.wts.tsrpc.server.manage.Manager;

import java.util.concurrent.TimeUnit;

public class ClientInvoker {

    private Manager manager;

    private ClientService clientService;

    private LoadBalancer loadBalancer;

    public ClientInvoker manager(Manager manager) {
        this.manager = manager;
        return this;
    }

    public ClientInvoker clientService(ClientService clientService) {
        this.clientService = clientService;
        return this;
    }

    public ClientInvoker loadBalancer(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
        return this;
    }

    public Object invoke(Object[] arguments) {
        String applicationId = clientService.getApplicationId();
        String serviceId = clientService.getServiceId();
        Endpoint endpoint = loadBalancer.balance(new BalanceAggregate(applicationId, serviceId));
        HttpClient httpClient = HttpClient.getHttpClient(endpoint);
        if (httpClient == null) {
            httpClient = HttpClient.addHttpClient(endpoint, (new HttpClient(endpoint.getHost(), endpoint.getPort(), 10)).manager(manager).init());
        }
        HttpClient finalHttpClient = httpClient;
        ServiceRequest request = manager.getTransform(clientService.getTransformType()).transformRequest(clientService, arguments);
        String message = manager.getTransform(clientService.getTransformType()).transformRequestToString(request);
        try {
            httpClient.connect().await(30, TimeUnit.SECONDS);
//            httpClient.connect().addListener(_ ->
//                    finalHttpClient.sendMsg(message)
//                            .await(30, TimeUnit.SECONDS)).sync();
            finalHttpClient.sendMsg(message)
                    .await(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ServiceResponse serviceResponse = HttpClientHandler.getServiceResponse(request.getRequestId());
        try {
            String body = manager.getTransform(clientService.getTransformType()).transformObjectToString(serviceResponse.getBody());
            return manager.getTransform(clientService.getTransformType()).transformReturnValueObject(body, clientService.getReturnGenericType());
        } finally {
            HttpClientHandler.removeServiceResponse(request.getRequestId());
        }


    }
}
