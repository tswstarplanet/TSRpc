package com.wts.tsrpc.common.registry;

import com.wts.tsrpc.server.manage.Application;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractRegistry implements Registry {
    private String serverList;

    private String namespace;

    private Map<Application, Boolean> subscribedApplications = new ConcurrentHashMap<>();

    private Map<String, List<ApplicationInstance>> instanceMap;

    private Object lock = new Object();

    public AbstractRegistry(String serverList, String namespace) {
        this.serverList = serverList;
        this.namespace = namespace;
        instanceMap = new ConcurrentHashMap<>();
    }

    @Override
    public List<ApplicationInstance> availableApplicationInstances(String applicationKey) {
        return instanceMap.get(applicationKey);
    }

    public String getServerList() {
        return serverList;
    }

    public void setServerList(String serverList) {
        this.serverList = serverList;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public Map<String, List<ApplicationInstance>> getInstanceMap() {
        return instanceMap;
    }

    public void setInstanceMap(Map<String, List<ApplicationInstance>> instanceMap) {
        this.instanceMap = instanceMap;
    }

    protected boolean checkApplicationSubscribed(Application application) {
        return subscribedApplications.get(application);
    }

    public Object getLock() {
        return lock;
    }

    protected void setApplicationSubscribed(Application application) {
        subscribedApplications.put(application, Boolean.TRUE);
    }
}
