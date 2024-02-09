package com.wts.tsrpc.common.registry;

import com.wts.tsrpc.server.manage.Application;

/**
 * Registry that register application to registration center
 */
public interface Registry {

    void register(Application application, RegisterConfig config);

    void deregister(Application application, RegisterConfig config);
}
