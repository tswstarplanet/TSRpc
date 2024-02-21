package com.wts.tsrpc.server.proxy;

public interface Invoker {
    Object invoke(Object[] arguments);
}
