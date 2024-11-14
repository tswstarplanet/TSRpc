package com.wts.tsrpc.client;

import com.wts.tsrpc.client.concurrent.ClientCallTask;
import com.wts.tsrpc.client.filter.ClientInvokerFilterChain;
import com.wts.tsrpc.client.loadbalance.LoadBalancer;
import com.wts.tsrpc.client.service.ClientMethod;
import com.wts.tsrpc.client.service.ClientService;
import com.wts.tsrpc.common.ServiceRequest;
import com.wts.tsrpc.common.ServiceResponse;
import com.wts.tsrpc.common.ServiceResponseCode;
import com.wts.tsrpc.common.Transformer;
import com.wts.tsrpc.exception.PanicException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ClientInvoker {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientInvoker.class);

    private ClientService clientService;

    private LoadBalancer loadBalancer;

    private ClientDispatcher clientDispatcher;

    private ExecutorService executorService;

    private Transformer transformer;

//    private Map<String, CompletableFuture<ServiceResponse>> pendingResponseMap = new ConcurrentHashMap<>();

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

    public ClientInvoker executorService(ExecutorService executorService) {
        this.executorService = executorService;
        return this;
    }

    public ClientInvoker transformer(Transformer transformer) {
        this.transformer = transformer;
        return this;
    }

    public Object invoke(Object[] arguments, ClientMethod method) {
//        method.settleGenericParamTypes();
//        String applicationId = clientService.getServiceApplicationId();
        var applicationInstance = loadBalancer.balance(clientService.getServiceApplication());
        HttpClient httpClient = HttpClient.getHttpClient(new Endpoint(applicationInstance.getHost(), applicationInstance.getPort()));
        if (httpClient == null) {
            httpClient = HttpClient.addHttpClient(new Endpoint(applicationInstance.getHost(), applicationInstance.getPort()), (new HttpClient(applicationInstance.getHost(), applicationInstance.getPort(), 10)).transformer(transformer).init());
        }
        ClientInvokerFilterChain invokerFilterChain = new ClientInvokerFilterChain();
        invokerFilterChain.invokerFilters(clientDispatcher.getDefaultClientInvokerFilters())
                .transformers(transformer)
                .clientService(clientService)
                .httpClient(httpClient);
        ServiceRequest request = transformer.transformRequest(clientService, arguments);
        request.setMethodName(method.getClientMethodName());
        request.setArgTypeNames(Arrays.stream(method.getArgTypes()).map(Class::getName).toList().toArray(new String[0]));
//        request.setArgTypeNames((String[]) Arrays.stream(method.getArgTypes()).map(Class::getName).toArray());
        request.setApplicationId(clientService.getServiceApplication().getApplicationId());
//        invokerFilterChain.doFilter(request, invokerFilterChain);

        ClientCallTask clientCallTask = new ClientCallTask(invokerFilterChain, request, clientService.getTimeout());
        Future<ServiceResponse> future = executorService.submit(clientCallTask);
        ServiceResponse serviceResponse;
        try {
            serviceResponse = future.get(clientService.getTimeout(), TimeUnit.MILLISECONDS);
            if (serviceResponse == null) {
                throw new PanicException("Service response is null !");
            } else if (!ServiceResponseCode.SUCCESS.getCode().equals(serviceResponse.getCode())) {
                throw new PanicException("Service invoke not success, error msg: " + serviceResponse.getMsg());
            }
            String body = transformer.transformObjectToString(serviceResponse.getBody());
            return transformer.transformReturnValueObject(body, method.getReturnGenericType());
        } catch (Exception e) {
            LOGGER.debug("Service invoke error: {}", e.getMessage());
            ClientInvokerResponseCache.getInstance().putException(request.getRequestId(), e);
            throw new PanicException(e.getMessage(), e);
        } finally {
            ClientInvokerResponseCache.getInstance().removeFuture(request.getRequestId());
        }

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


//        ServiceResponse serviceResponse = HttpClientHandler.getServiceResponse(request.getRequestId());
//        try {
//            String body = transformer.transformObjectToString(serviceResponse.getBody());
//            return transformer.transformReturnValueObject(body, method.getReturnGenericType());
//        } finally {
//            HttpClientHandler.removeServiceResponse(request.getRequestId());
//        }


    }
}
