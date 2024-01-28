package com.wts.tsrpc.server.filter;

import com.wts.tsrpc.common.ServiceRequest;
import com.wts.tsrpc.common.ServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogInvokerFilter implements InvokerFilter {

    private static final Logger logger = LoggerFactory.getLogger(LogInvokerFilter.class);

    @Override
    public void doFilter(ServiceRequest request, ServiceResponse response, InvokerFilterChain filterChain) {
        logger.info("Log invoker Filter !");
        filterChain.doFilter(request, response, filterChain);
    }
}
