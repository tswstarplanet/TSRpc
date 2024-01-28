package com.wts.tsrpc.server.filter;

import com.wts.tsrpc.common.ServiceRequest;
import com.wts.tsrpc.common.ServiceResponse;

public interface InvokerFilter {
    void doFilter(ServiceRequest request, ServiceResponse response, InvokerFilterChain filterChain);
}
