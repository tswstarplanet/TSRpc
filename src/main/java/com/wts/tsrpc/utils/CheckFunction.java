package com.wts.tsrpc.utils;

import com.wts.tsrpc.service.Service;

import java.util.function.BiFunction;
import java.util.function.Function;

public class CheckFunction<T, U, R> {
    public static void check(BiFunction<String, Service, Boolean> function, String serviceId, Service service, String msg) {
        Boolean result = function.apply(serviceId, service);
    }
}
