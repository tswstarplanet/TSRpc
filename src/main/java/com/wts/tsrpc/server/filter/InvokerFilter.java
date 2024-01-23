package com.wts.tsrpc.server.filter;

import com.wts.tsrpc.server.service.ServiceRequest;
import com.wts.tsrpc.server.service.ServiceResponse;

public interface InvokerFilter {
    void doFilter(ServiceRequest request, ServiceResponse response, InvokerFilterChain filterChain);
}
