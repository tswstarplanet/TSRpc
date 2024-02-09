package com.wts.tsrpc.server.filter;

import com.wts.tsrpc.common.ServiceRequest;
import com.wts.tsrpc.common.ServiceResponse;

public class ClientHelperFilter implements ServerInvokerFilter {
    @Override
    public void doFilter(ServiceRequest request, ServiceResponse response, ServerInvokerFilterChain filterChain) {
        response.setApplicationId(request.getApplicationId());
        response.setServiceId(request.getServiceId());
        filterChain.doFilter(request, response, filterChain);
    }
}
