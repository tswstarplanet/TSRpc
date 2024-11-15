package com.wts.tsrpc.common.registry;

import com.wts.tsrpc.client.Endpoint;
import com.wts.tsrpc.server.manage.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractRegistry implements Registry {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRegistry.class);

    private String serverList;

    private String namespace;

    private Application application;

    private Endpoint endpoint;

    private Map<Application, Boolean> subscribedApplications = new ConcurrentHashMap<>();

    private Map<String, List<ApplicationInstance>> instanceMap;

    private Object lock = new Object();

    public AbstractRegistry(String serverList, String namespace, Application application, Endpoint endpoint) {
        this.serverList = serverList;
        this.namespace = namespace;
        this.application = application;
        this.endpoint = endpoint;
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

    protected Boolean checkApplicationSubscribed(Application application) {
        return Boolean.TRUE.equals(subscribedApplications.get(application));
    }

    public Object getLock() {
        return lock;
    }

    protected void setApplicationSubscribed(Application application) {
        subscribedApplications.put(application, Boolean.TRUE);
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }
}
