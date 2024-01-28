package com.wts.tsrpc.common.utils;

import java.util.function.Predicate;

public enum Checker {
    HTTP_METHOD_CHECK(method -> ((String) method).equals("POST"));

    private final Predicate<Object> predicate;

    Checker(Predicate<Object> predicate) {
        this.predicate = predicate;
    }

    public Predicate<Object> getPredicate() {
        return predicate;
    }
}
