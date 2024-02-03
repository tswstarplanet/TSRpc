package com.wts.tsrpc.client.filter;

import com.wts.tsrpc.common.ServiceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogClientInvokerFilter implements ClientInvokerFilter {

    private static final Logger logger = LoggerFactory.getLogger(LogClientInvokerFilter.class);

    @Override
    public void doFilter(ServiceRequest request, ClientInvokerFilterChain filterChain) {
        logger.info("Log client invoker filter start !");
        filterChain.doFilter(request, filterChain);
        logger.info("Log client invoker filter end !");
    }
}
