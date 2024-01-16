package com.wts.tsrpc.manage;

import com.wts.tsrpc.service.ServiceRequest;
import com.wts.tsrpc.service.ServiceResponse;

public interface Dispatcher {
    ServiceResponse dispatch(ServiceRequest request);
}
