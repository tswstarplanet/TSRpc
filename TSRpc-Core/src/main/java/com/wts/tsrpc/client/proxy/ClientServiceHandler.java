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

package com.wts.tsrpc.client.proxy;

import com.wts.tsrpc.client.service.ClientService;
import com.wts.tsrpc.exception.PanicException;
import org.slf4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Client service handler
 * used to wrapper the actually proxy object of service
 */
public class ClientServiceHandler implements InvocationHandler {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ClientServiceHandler.class);

    /**
     * Client service interface type
     */
    private Class<?> interfaceType;

    /**
     * Client service
     */
    private ClientService clientService;

    /**
     * Client service target object
     * First is null, then will be set the actually proxy object of service
     */
    private Object target;

    public ClientServiceHandler() {
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        LOGGER.debug("Before call client service method, service: {}, method: {}", proxy.getClass().getName(), method.getName());
        try {
            Object result = method.invoke(target, args);
            LOGGER.debug("After call client service method, service: {}, method: {}", proxy.getClass().getName(), method.getName());
            return result;
        } catch (Throwable e) {
            LOGGER.error("Error: " + e.getMessage(), e);
            throw new PanicException(e.getCause());
        }
    }

    public Class<?> getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(Class<?> interfaceType) {
        this.interfaceType = interfaceType;
    }

    public ClientService getClientService() {
        return clientService;
    }

    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }
}
