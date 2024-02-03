package com.wts.tsrpc.server.filter;

import com.wts.tsrpc.common.ServiceRequest;
import com.wts.tsrpc.common.ServiceResponse;

public interface ServerInvokerFilter {
    void doFilter(ServiceRequest request, ServiceResponse response, ServerInvokerFilterChain filterChain);
}
