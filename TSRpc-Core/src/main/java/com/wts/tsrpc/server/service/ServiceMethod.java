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

    private Class<?>[] argTypes;

    private Type[] parameterTypes;

    private Class<?> returnType;

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

    public Class<?>[] getArgTypes() {
        return argTypes;
    }

    public void setArgTypes(Class<?>[] argTypes) {
        this.argTypes = argTypes;
    }

    public Type[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Type[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
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
