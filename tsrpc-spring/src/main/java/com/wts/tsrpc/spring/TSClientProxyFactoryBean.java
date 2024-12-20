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
import com.wts.tsrpc.client.service.ClientService;
import com.wts.tsrpc.client.utils.NameUtils;
import com.wts.tsrpc.common.utils.ReflectUtils;
import com.wts.tsrpc.server.manage.Application;
import org.springframework.beans.factory.SmartFactoryBean;

import java.lang.reflect.Proxy;

public class TSClientProxyFactoryBean<T> implements SmartFactoryBean<T> {

    /**
     * Interface type
     */
    private Class<T> interfaceType;

    /**
     * Application id
     */
    private String applicationId;

    /**
     * Application version
     */
    private String applicationVersion;

    /**
     * Service id
     */
    private String serviceId;

    @Override
    public T getObject() throws Exception {
        ClientServiceHandler clientServiceHandler = new ClientServiceHandler();
        clientServiceHandler.setInterfaceType(interfaceType);
        ClientService clientService = new ClientService();
        Application application = new Application().applicationId(applicationId).version(applicationVersion);
        clientService.setServiceApplication(application);
        clientService.setServiceId(serviceId);
        clientService.setClientClassFullName(interfaceType.getName());
        clientService.setClientMethods(ReflectUtils.getClientMethods(interfaceType));
        clientServiceHandler.setClientService(clientService);
        ClientServiceStorage.getInstance().addClientServiceClass(NameUtils.applicationKey(applicationId, applicationVersion), serviceId, interfaceType)
                .addClientServiceHandler(NameUtils.serviceBeanName(applicationId, applicationVersion, serviceId), clientServiceHandler);
        return (T) Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class[]{interfaceType}, clientServiceHandler);
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isEagerInit() {
        return true;
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
