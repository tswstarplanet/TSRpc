package com.wts.tsrpc.common.registry;

/**
 * Service register config
 */
public class RegisterConfig {

    private String serverList;

    private String namespace;

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
}
