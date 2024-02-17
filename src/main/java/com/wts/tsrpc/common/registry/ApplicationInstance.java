package com.wts.tsrpc.common.registry;

public class ApplicationInstance {

    private String applicationId;

    private String version;

    private String ip;

    private Integer port;

    private String applicationKey;

    public ApplicationInstance(String applicationId, String version, String ip, Integer port) {
        this.applicationId = applicationId;
        this.version = version;
        this.ip = ip;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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
