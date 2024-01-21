package com.wts.tsrpc.server.service;

public interface Transformer {
    ServiceRequest transform(String body);
}
