package com.wts.tsrpc.client.filter;

import com.wts.tsrpc.common.ServiceRequest;
import com.wts.tsrpc.common.ServiceResponse;

import java.util.concurrent.CompletableFuture;

public interface ClientInvokerFilter {
    void doFilter(ServiceRequest request, ClientInvokerFilterChain filterChain, CompletableFuture<ServiceResponse> completableFuture);
}
