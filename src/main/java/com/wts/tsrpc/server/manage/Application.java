package com.wts.tsrpc.server.manage;

public class Application {
    private String applicationId;

    private String version;

    private static final Application application = new Application();

    public Application() {

    }

    public static Application getApplication() {
        return application;
    }

    public Application name(String name) {
        this.applicationId = name;
        return this;
    }

    public Application version(String version) {
        this.version = version;
        return this;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public String getVersion() {
        return version;
    }

    public String getKey() {
        return STR."\{applicationId}:\{version}";
    }
}
