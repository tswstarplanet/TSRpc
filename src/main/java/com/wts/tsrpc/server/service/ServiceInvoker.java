package com.wts.tsrpc.server.service;

public interface ServiceInvoker {
    void invoke(ServiceRequest request, ServiceResponse response, Service service);
}
