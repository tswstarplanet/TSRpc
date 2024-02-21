package com.wts.tsrpc.common.registry;

public class ApplicationInstance {

    private String applicationId;

    private String version;

    private String host;

    private Integer port;

    private String applicationKey;

    public ApplicationInstance(String applicationId, String version, String host, Integer port) {
        this.applicationId = applicationId;
        this.version = version;
        this.host = host;
        this.port = port;
        this.applicationKey = STR."\{applicationId}:\{version}";
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getApplicationKey() {
        return applicationKey;
    }

    public void setApplicationKey(String applicationKey) {
        this.applicationKey = applicationKey;
    }
}
