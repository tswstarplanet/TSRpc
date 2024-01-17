package com.wts.tsrpc.service;

public class Service {
    private String classFullName;

    private String methodName;

    private Class<?>[] argTypes;

    private Class<?> returnType;

    public Service classFullName(String classFullName) {
        this.classFullName = classFullName;
        return this;
    }

    public Service methodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public Service argTypes(Class<?>[] argTypes) {
        this.argTypes = argTypes;
        return this;
    }

    public Service returnType(Class<?> returnType) {
        this.returnType = returnType;
        return this;
    }

    public String getClassFullName() {
        return classFullName;
    }

    public void setClassFullName(String classFullName) {
        this.classFullName = classFullName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getArgTypes() {
        return argTypes;
    }

    public void setArgTypes(Class<?>[] argTypes) {
        this.argTypes = argTypes;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }
}
