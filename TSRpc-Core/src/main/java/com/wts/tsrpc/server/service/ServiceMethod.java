package com.wts.tsrpc.server.service;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;

/**
 * Server side service method model
 */
public class ServiceMethod {

    /**
     * The name of service method
     */
    private String methodName;

    /**
     * The types of service method arguments
     */
    private Class<?>[] argTypes;

    /**
     * The generic types of service method parameters
     */
    private Type[] parameterTypes;

    /**
     * The return type of service method
     */
    private Class<?> returnType;

    public ServiceMethod() {
    }

    public ServiceMethod(String methodName) {
        this.methodName = methodName;
    }

    public ServiceMethod(String methodName, Class<?>[] argTypes) {
        this.methodName = methodName;
        this.argTypes = argTypes;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public ServiceMethod methodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public Class<?>[] getArgTypes() {
        return argTypes;
    }

    public void setArgTypes(Class<?>[] argTypes) {
        this.argTypes = argTypes;
    }

    public ServiceMethod argTypes(Class<?>[] argTypes) {
        this.argTypes = argTypes;
        return this;
    }

    public Type[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Type[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public ServiceMethod parameterTypes(Type[] parameterTypes) {
        this.parameterTypes = parameterTypes;
        return this;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    public ServiceMethod returnType(Class<?> returnType) {
        this.returnType = returnType;
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ServiceMethod that = (ServiceMethod) object;
        return Objects.equals(methodName, that.methodName) && Arrays.equals(argTypes, that.argTypes) && Arrays.equals(parameterTypes, that.parameterTypes) && Objects.equals(returnType, that.returnType);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(methodName, returnType);
        result = 31 * result + Arrays.hashCode(argTypes);
        result = 31 * result + Arrays.hashCode(parameterTypes);
        return result;
    }
}
