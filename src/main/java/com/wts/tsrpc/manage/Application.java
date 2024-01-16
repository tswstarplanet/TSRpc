package com.wts.tsrpc.manage;

import org.checkerframework.checker.units.qual.A;

public class Application {
    private String name;

    private String version;

    private static final Application application = new Application();

    private Application() {

    }

    public static Application getApplication() {
        return application;
    }

    public Application name(String name) {
        this.name = name;
        return this;
    }

    public Application version(String version) {
        this.version = version;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }
}
