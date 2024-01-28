package com.wts.tsrpc.server.service;

import com.wts.tsrpc.common.ServiceRequest;
import com.wts.tsrpc.common.ServiceResponse;
import com.wts.tsrpc.common.Service;

public interface ServiceInvoker {
    void invoke(ServiceRequest request, ServiceResponse response, Service service);
}
