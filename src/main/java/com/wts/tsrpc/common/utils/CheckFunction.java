package com.wts.tsrpc.common.utils;

import com.wts.tsrpc.common.Service;

import java.util.function.BiFunction;

public class CheckFunction<T, U, R> {
    public static void check(BiFunction<String, Service, Boolean> function, String serviceId, Service service, String msg) {
        Boolean result = function.apply(serviceId, service);
    }
}
