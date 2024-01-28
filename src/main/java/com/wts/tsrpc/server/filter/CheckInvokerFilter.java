package com.wts.tsrpc.server.filter;

import com.wts.tsrpc.common.ServiceRequest;
import com.wts.tsrpc.common.ServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckInvokerFilter implements InvokerFilter {

    private static final Logger logger = LoggerFactory.getLogger(CheckInvokerFilter.class);

    @Override
    public void doFilter(ServiceRequest request, ServiceResponse response, InvokerFilterChain filterChain) {
        logger.info("Check invoker filter !");
        filterChain.doFilter(request, response, filterChain);
    }
}
