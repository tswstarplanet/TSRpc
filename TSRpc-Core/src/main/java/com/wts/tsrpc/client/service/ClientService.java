package com.wts.tsrpc.client.service;

import com.wts.tsrpc.common.utils.CollectionUtils;
import com.wts.tsrpc.common.utils.ReflectUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientService {

    private String transformType;

    private String clientServiceId;

    private String applicationId;

    private String applicationVersion;

    private String serviceId;

    private String clientClassFullName;

    private List<ClientMethod> clientMethods = new ArrayList<>();

    private Map<String, ClientMethod> clientMethodMap = new HashMap<>();

    public ClientService settleClientMethod() {
        if (CollectionUtils.isEmpty(clientMethods)) {
            return this;
        }
        for (ClientMethod method : clientMethods) {
            clientMethodMap.put(ReflectUtils.getMethodSignature(method.getClientMethodName(), method.getArgTypes()), method);
        }
        return this;
    }

    public Type getMethodReturnGenericType(String signature) {
        return clientMethodMap.get(signature).getReturnGenericType();
    }

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

    public String getClientClassFullName() {
        return clientClassFullName;
    }

    public void setClientClassFullName(String clientClassFullName) {
        this.clientClassFullName = clientClassFullName;
    }

    public List<ClientMethod> getClientMethods() {
        return clientMethods;
    }

    public void setClientMethods(List<ClientMethod> clientMethods) {
        this.clientMethods = clientMethods;
    }

    public Map<String, ClientMethod> getClientMethodMap() {
        return clientMethodMap;
    }

    public void setClientMethodMap(Map<String, ClientMethod> clientMethodMap) {
        this.clientMethodMap = clientMethodMap;
    }
}
