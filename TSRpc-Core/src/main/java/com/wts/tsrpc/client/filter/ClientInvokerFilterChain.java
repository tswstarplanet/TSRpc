package com.wts.tsrpc.client.filter;

import com.wts.tsrpc.client.HttpClient;
import com.wts.tsrpc.client.service.ClientService;
import com.wts.tsrpc.common.ServiceRequest;
import com.wts.tsrpc.common.ServiceResponse;
import com.wts.tsrpc.common.transform.Transformers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ClientInvokerFilterChain implements ClientInvokerFilter {
    private int pos = 0;

    private ClientService clientService;

    private HttpClient httpClient;

    private Transformers transformers;

    private List<ClientInvokerFilter> invokerFilters = new ArrayList<>();

    public void addFilter(ClientInvokerFilter invokerFilter) {
        invokerFilters.add(invokerFilter);
    }

    @Override
    public void doFilter(ServiceRequest serviceRequest, ClientInvokerFilterChain filterChain) {
        if (pos == invokerFilters.size()) {
//            ServiceRequest request = manager.getTransform(clientService.getTransformType()).transformRequest(clientService, arguments);
            String message = transformers.getTransform(clientService.getTransformType()).transformRequestToString(serviceRequest);
            try {
                httpClient.connect().await(30, TimeUnit.SECONDS);
//            httpClient.connect().addListener(_ ->
//                    finalHttpClient.sendMsg(message)
//                            .await(30, TimeUnit.SECONDS)).sync();
                httpClient.sendMsg(message)
                        .await(30, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        invokerFilters.get(pos++).doFilter(serviceRequest, filterChain);
    }

    private void sendMsg(ServiceRequest request, ServiceResponse response) {

    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public ClientService getClientService() {
        return clientService;
    }

    public ClientInvokerFilterChain clientService(ClientService clientService) {
        this.clientService = clientService;
        return this;
    }

    public Transformers getTransformers() {
        return transformers;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public ClientInvokerFilterChain httpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }

    public List<ClientInvokerFilter> getInvokerFilters() {
        return invokerFilters;
    }

    public ClientInvokerFilterChain invokerFilters(List<ClientInvokerFilter> invokerFilters) {
        this.invokerFilters = invokerFilters;
        return this;
    }

    public ClientInvokerFilterChain transformers(Transformers transformers) {
        this.transformers = transformers;
        return this;
    }
}
