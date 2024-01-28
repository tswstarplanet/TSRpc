package com.wts.tsrpc.client;

import com.wts.tsrpc.common.ServiceRequest;
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
            httpClient = HttpClient.addHttpClient(endpoint, new HttpClient(endpoint.getHost(), endpoint.getPort(), 10));
        }
        HttpClient finalHttpClient = httpClient;
        ServiceRequest request = manager.getTransform(clientService.getTransformType()).transformRequest(clientService, arguments);

        httpClient.connect().addListener(_ ->
                finalHttpClient.sendMsg(manager.getTransform(clientService.getTransformType()).transformRequestToString(request))
                        .await(30, TimeUnit.SECONDS));
        try {
            return HttpClientHandler.getServiceResponse(request.getRequestId());
        } finally {
            HttpClientHandler.removeServiceResponse(request.getRequestId());
        }

    }
}
