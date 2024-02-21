package com.wts.tsrpc.client.filter;

import com.wts.tsrpc.common.ServiceRequest;

public interface ClientInvokerFilter {
    void doFilter(ServiceRequest request, ClientInvokerFilterChain filterChain);
}
