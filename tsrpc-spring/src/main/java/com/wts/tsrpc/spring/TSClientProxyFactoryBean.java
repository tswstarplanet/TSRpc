/*
 * Copyright 2024 wts
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wts.tsrpc.spring;

import com.wts.tsrpc.client.ClientServiceStorage;
import com.wts.tsrpc.client.proxy.ClientServiceHandler;
import com.wts.tsrpc.client.utils.NameUtils;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

public class TSClientProxyFactoryBean<T> implements FactoryBean<T> {

    private Class<T> interfaceType;

    private String applicationId;

    private String applicationVersion;

    private String serviceId;

    @Override
    public T getObject() throws Exception {

        ClientServiceHandler clientServiceHandler = new ClientServiceHandler();
        ClientServiceStorage.getInstance().addClientServiceClass(NameUtils.applicationKey(applicationId, applicationVersion), serviceId, interfaceType)
                .addClientServiceHandler(NameUtils.applicationKey(applicationId, applicationVersion), serviceId, clientServiceHandler);
        return (T) Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class[]{interfaceType}, clientServiceHandler);
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    public Class<T> getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(Class<T> interfaceType) {
        this.interfaceType = interfaceType;
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
}
