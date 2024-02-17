package com.wts.tsrpc.common.registry;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractRegistry {
    private String serverList;

    private String namespace;

    private Map<String, List<ApplicationInstance>> instanceMap;

    public AbstractRegistry(String serverList, String namespace) {
        this.serverList = serverList;
        this.namespace = namespace;
        instanceMap = new ConcurrentHashMap<>();
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
}
