package com.wts.tsrpc.client;

public interface LoadBalancer {
    Endpoint balance(BalanceAggregate aggregate);
}
