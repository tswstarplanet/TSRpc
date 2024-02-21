package com.wts.tsrpc.client.loadbalance;

import com.wts.tsrpc.common.registry.ApplicationInstance;
import com.wts.tsrpc.server.manage.Application;

public interface LoadBalancer {
    ApplicationInstance balance(Application application);
}
