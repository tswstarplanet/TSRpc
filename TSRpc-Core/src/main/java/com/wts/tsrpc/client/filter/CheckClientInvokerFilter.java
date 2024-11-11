package com.wts.tsrpc.client.filter;

import com.wts.tsrpc.common.ServiceRequest;
import com.wts.tsrpc.common.ServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class CheckClientInvokerFilter implements ClientInvokerFilter {

    private static final Logger logger = LoggerFactory.getLogger(CheckClientInvokerFilter.class);

    @Override
    public void doFilter(ServiceRequest request, ClientInvokerFilterChain filterChain, CompletableFuture<ServiceResponse> completableFuture) {
        logger.info("Check client invoker filter start !");
        filterChain.doFilter(request, filterChain, completableFuture);
        logger.info("Check client invoker filter end !");
    }
}
