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

package com.wts.tsrpc.client;

/**
 * ClientInstance is a class that represents a client instance to wrapper the client service interface class
 */
public class ClientInstance {

    private Class<?> clientServiceInterface;

    private String applicationId;

    private String applicationVersion;

    private String serviceId;

    public ClientInstance() {
    }

    public ClientInstance(Class<?> clientServiceInterface, String applicationId, String applicationVersion, String serviceId) {
        this.clientServiceInterface = clientServiceInterface;
        this.applicationId = applicationId;
        this.applicationVersion = applicationVersion;
        this.serviceId = serviceId;
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

    public Class<?> getClientServiceInterface() {
        return clientServiceInterface;
    }

    public void setClientServiceInterface(Class<?> clientServiceInterface) {
        this.clientServiceInterface = clientServiceInterface;
    }
}
