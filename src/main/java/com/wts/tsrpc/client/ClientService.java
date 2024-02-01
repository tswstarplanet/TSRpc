package com.wts.tsrpc.client;

import com.wts.tsrpc.server.service.ParameterType;

import java.lang.reflect.Type;
import java.util.List;

public class ClientService {

    private String transformType;

    private String clientServiceId;

    private String applicationId;

    private String applicationVersion;

    private String serviceId;

    private List<Type> paramTypes;

    private String clientClassFullName;

    private String clientMethodName;

    private Class<?>[] argTypes;

    private ParameterType[] parameterTypes;

    private Class<?> returnType;

    private Type returnGenericType;

    public String getTransformType() {
        return transformType;
    }

    public void setTransformType(String transformType) {
        this.transformType = transformType;
    }

    public String getClientServiceId() {
        return clientServiceId;
    }

    public void setClientServiceId(String clientServiceId) {
        this.clientServiceId = clientServiceId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public List<Type> getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(List<Type> paramTypes) {
        this.paramTypes = paramTypes;
    }

    public String getClientClassFullName() {
        return clientClassFullName;
    }

    public void setClientClassFullName(String clientClassFullName) {
        this.clientClassFullName = clientClassFullName;
    }

    public String getClientMethodName() {
        return clientMethodName;
    }

    public void setClientMethodName(String clientMethodName) {
        this.clientMethodName = clientMethodName;
    }

    public Class<?>[] getArgTypes() {
        return argTypes;
    }

    public void setArgTypes(Class<?>[] argTypes) {
        this.argTypes = argTypes;
    }

    public ParameterType[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(ParameterType[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    public Type getReturnGenericType() {
        return returnGenericType;
    }

    public void setReturnGenericType(Type returnGenericType) {
        this.returnGenericType = returnGenericType;
    }
}
