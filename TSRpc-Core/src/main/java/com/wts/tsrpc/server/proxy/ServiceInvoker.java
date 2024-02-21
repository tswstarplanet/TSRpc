package com.wts.tsrpc.server.proxy;

import com.wts.tsrpc.common.ServiceRequest;
import com.wts.tsrpc.common.ServiceResponse;
import com.wts.tsrpc.server.service.Service;

public interface ServiceInvoker {

    void invoke(ServiceRequest request, ServiceResponse response, Service service);
}
