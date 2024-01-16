package com.wts.tsrpc.service;

public interface Transformer {
    ServiceRequest transform(String transformType);
}
