package com.wts.tsrpc.test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class MyParameterizedType implements ParameterizedType {

    private Type[] args;

    private Class rawType;

    public MyParameterizedType( Class rawType,Type[] args) {
        this.args = args;
        this.rawType = rawType;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return args;
    }

    @Override
    public Type getRawType() {
        return rawType;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }
}
