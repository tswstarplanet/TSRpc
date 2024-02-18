package com.wts.tsrpc.server.manage;

public class Application {
    private String applicationId;

    private String version;

    private static final Application application = new Application();

    public Application() {

    }

    public Application(String applicationId, String version) {
        this.applicationId = applicationId;
        this.version = version;
    }

    public static Application getApplication() {
        return application;
    }

    public Application applicationId(String name) {
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
