package com.wts.tsrpc.server.manage;

import com.wts.tsrpc.server.service.ServiceRequest;
import com.wts.tsrpc.server.service.ServiceResponse;

public interface Dispatcher {
    ServiceResponse dispatch(ServiceRequest request);
}
