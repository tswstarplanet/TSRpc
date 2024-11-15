package com.wts.tsrpc.common.registry;

import com.google.common.collect.Lists;
import com.wts.tsrpc.server.manage.Application;

import java.util.List;

/**
 * Registry that register application to registration center
 */
public interface Registry {

    default void register() {}

    default void deregister() {}

    default void subscribe(Application application) {}

    default List<ApplicationInstance> availableApplicationInstances(String applicationKey) { return Lists.newArrayList(); }

    void init();

    default void destroy() {}
}
