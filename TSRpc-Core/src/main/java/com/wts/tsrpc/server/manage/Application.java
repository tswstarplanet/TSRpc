package com.wts.tsrpc.server.manage;

import java.util.Objects;

public class Application {
    private String applicationId;

    private String version;

//    private static final Application application = new Application();

    public Application() {

    }

    public Application(String applicationId, String version) {
        this.applicationId = applicationId;
        this.version = version;
    }

//    public static Application getApplication() {
//        return application;
//    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Application that = (Application) o;
        return Objects.equals(applicationId, that.applicationId) && Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicationId, version);
    }
}
