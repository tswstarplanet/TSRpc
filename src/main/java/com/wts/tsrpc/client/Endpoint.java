package com.wts.tsrpc.client;

import java.util.Objects;

public final class Endpoint {
    private final String host;

    private final Integer port;

    public Endpoint(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Endpoint endpoint = (Endpoint) object;
        return Objects.equals(host, endpoint.host) && Objects.equals(port, endpoint.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port);
    }
}
