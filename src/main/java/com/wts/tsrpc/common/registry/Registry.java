package com.wts.tsrpc.common.registry;

import com.wts.tsrpc.client.Endpoint;
import com.wts.tsrpc.server.manage.Application;

/**
 * Registry that register application to registration center
 */
public interface Registry {

    default void register(Application application, Endpoint endpoint) {}

    default void deregister(Application application, Endpoint endpoint) {}

    default void subscribe(Application application) {}
}
