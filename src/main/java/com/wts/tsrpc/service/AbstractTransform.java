package com.wts.tsrpc.service;

import com.wts.tsrpc.manage.Manager;

public class AbstractTransform implements Transformer {
    private Manager manager;

    public Transformer manager(Manager manager) {
        this.manager = manager;
        return this;
    }

    @Override
    public ServiceRequest transform(String body) {
        return null;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }
}
