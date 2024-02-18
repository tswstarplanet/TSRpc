package com.wts.tsrpc.client.loadbalance;

import com.wts.tsrpc.common.registry.ApplicationInstance;
import com.wts.tsrpc.common.registry.Registry;
import com.wts.tsrpc.common.utils.CollectionUtils;
import com.wts.tsrpc.server.manage.Application;

import java.util.concurrent.ThreadLocalRandom;

public class RandomLoadBalancer extends AbstractLoadBalancer {

    public RandomLoadBalancer(Registry registry) {
        super(registry);
    }

    @Override
    public ApplicationInstance balance(Application application) {
        var applicationInstances = super.getRegistry().availableApplicationInstances(application.getKey());
        if (CollectionUtils.isEmpty(applicationInstances)) {
            return null;
        } else {
            var index = ThreadLocalRandom.current().nextInt(0, applicationInstances.size());
            return applicationInstances.get(index);
        }
    }
}
