package com.wts.tsrpc.manage;

public enum SerializationType {
    GSON("gson");
    private final String type;

    private SerializationType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
