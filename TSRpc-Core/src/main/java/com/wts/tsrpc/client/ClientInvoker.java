package com.wts.tsrpc.client;

import com.wts.tsrpc.client.filter.ClientInvokerFilterChain;
import com.wts.tsrpc.client.loadbalance.LoadBalancer;
import com.wts.tsrpc.client.service.ClientMethod;
import com.wts.tsrpc.client.service.ClientService;
import com.wts.tsrpc.common.ServiceRequest;
import com.wts.tsrpc.common.ServiceResponse;
import com.wts.tsrpc.common.transform.Transformers;
import com.wts.tsrpc.server.manage.Application;

import java.util.Arrays;

public class ClientInvoker {

    private ClientService clientService;

    private LoadBalancer loadBalancer;

    private ClientDispatcher clientDispatcher;

    private Transformers transformers;

    public ClientInvoker clientService(ClientService clientService) {
        this.clientService = clientService;
        return this;
    }

    public ClientInvoker loadBalancer(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
        return this;
    }

    public ClientInvoker clientDispatcher(ClientDispatcher clientDispatcher) {
        this.clientDispatcher = clientDispatcher;
        return this;
    }

    public Object invoke(Object[] arguments, ClientMethod method) {
        method.settleGenericParamTypes();
        String applicationId = clientService.getApplicationId();
        var applicationInstance = loadBalancer.balance(new Application(applicationId, clientService.getApplicationVersion()));
        HttpClient httpClient = HttpClient.getHttpClient(new Endpoint(applicationInstance.getHost(), applicationInstance.getPort()));
        if (httpClient == null) {
            httpClient = HttpClient.addHttpClient(new Endpoint(applicationInstance.getHost(), applicationInstance.getPort()), (new HttpClient(applicationInstance.getHost(), applicationInstance.getPort(), 10)).transformers(transformers).init());
        }
        ClientInvokerFilterChain invokerFilterChain = new ClientInvokerFilterChain();
        invokerFilterChain.invokerFilters(clientDispatcher.getDefaultClientInvokerFilters())
                .transformers(transformers)
                .clientService(clientService)
                .httpClient(httpClient);
        ServiceRequest request = transformers.getTransform(clientService.getTransformType()).transformRequest(clientService, arguments);
        request.setMethodName(method.getClientMethodName());
        request.setArgTypeNames(Arrays.stream(method.getArgTypes()).map(Class::getName).toList().toArray(new String[0]));
//        request.setArgTypeNames((String[]) Arrays.stream(method.getArgTypes()).map(Class::getName).toArray());
        request.setApplicationId(clientService.getApplicationId());
        invokerFilterChain.doFilter(request, invokerFilterChain);
//        ServiceRequest request = manager.getTransform(clientService.getTransformType()).transformRequest(clientService, arguments);
//        String message = manager.getTransform(clientService.getTransformType()).transformRequestToString(request);
//        try {
//            httpClient.connect().await(30, TimeUnit.SECONDS);
////            httpClient.connect().addListener(_ ->
////                    finalHttpClient.sendMsg(message)
////                            .await(30, TimeUnit.SECONDS)).sync();
//            finalHttpClient.sendMsg(message)
//                    .await(30, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        ServiceResponse serviceResponse = HttpClientHandler.getServiceResponse(request.getRequestId());
        try {
            String body = transformers.getTransform(clientService.getTransformType()).transformObjectToString(serviceResponse.getBody());
            return transformers.getTransform(clientService.getTransformType()).transformReturnValueObject(body, method.getReturnGenericType());
        } finally {
            HttpClientHandler.removeServiceResponse(request.getRequestId());
        }


    }
}
