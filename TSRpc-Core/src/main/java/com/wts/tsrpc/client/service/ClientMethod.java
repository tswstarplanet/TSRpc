package com.wts.tsrpc.client.service;

import com.wts.tsrpc.exception.BizException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Server side service method model
 */
public class ClientMethod {

    private List<Type> paramTypes;

    private String clientClassFullName;

    private String clientMethodName;

    private Class<?>[] argTypes;

    private Class<?> returnType;

    private Type returnGenericType;

    public void settleGenericParamTypes() {
        if (StringUtils.isNoneEmpty(clientClassFullName, clientMethodName) && ArrayUtils.isNotEmpty(argTypes)) {
            try {
                Method method = Class.forName(clientClassFullName).getMethod(clientMethodName, argTypes);
                this.returnType = method.getReturnType();
                this.returnGenericType = method.getGenericReturnType();
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<Type> getParamTypes() {
        return paramTypes;
    }

    public ClientMethod setParamTypes(List<Type> paramTypes) {
        this.paramTypes = paramTypes;
        return this;
    }

    public String getClientClassFullName() {
        return clientClassFullName;
    }

    public ClientMethod clientClassFullName(String clientClassFullName) {
        this.clientClassFullName = clientClassFullName;
        return this;
    }

    public String getClientMethodName() {
        return clientMethodName;
    }

    public ClientMethod clientMethodName(String clientMethodName) {
        this.clientMethodName = clientMethodName;
        return this;
    }

    public Class<?>[] getArgTypes() {
        return argTypes;
    }

    public ClientMethod argTypes(Class<?>[] argTypes) {
        this.argTypes = argTypes;
        return this;
    }

    public ClientMethod argTypes(String[] argTypeNames) {
        if (ArrayUtils.isEmpty(argTypeNames)) {
            this.argTypes = new Class[0];
            return this;
        }
        Class<?>[] classes = new Class[argTypeNames.length];
        for (int i = 0; i < argTypeNames.length; i++) {
            Class<?> aClass = null;
            try {
                classes[i] = Class.forName(argTypeNames[i]);
            } catch (ClassNotFoundException e) {
                throw new BizException("");
            }
        }
        this.argTypes = classes;
        return this;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public ClientMethod returnType(Class<?> returnType) {
        this.returnType = returnType;
        return this;
    }

    public Type getReturnGenericType() {
        return returnGenericType;
    }

    public ClientMethod returnGenericType(Type returnGenericType) {
        this.returnGenericType = returnGenericType;
        return this;
    }
}
