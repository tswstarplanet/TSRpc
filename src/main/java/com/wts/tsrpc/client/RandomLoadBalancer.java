package com.wts.tsrpc.client;

public class RandomLoadBalancer implements LoadBalancer {
    @Override
    public Endpoint balance(BalanceAggregate aggregate) {
        return new Endpoint("localhost", 8866);
    }
}
