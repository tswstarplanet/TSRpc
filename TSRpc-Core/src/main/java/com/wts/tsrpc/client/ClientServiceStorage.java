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

import com.wts.tsrpc.client.proxy.ClientServiceHandler;
import com.wts.tsrpc.client.service.ClientMethod;
import com.wts.tsrpc.client.service.ClientService;
import com.wts.tsrpc.exception.PanicException;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ClientServiceStorage {

    private final Map<String, Map<String, ClientService>> clientServiceMap = new ConcurrentHashMap<>();

    // applicationKey -> serviceId -> Class
    private final Map<String, Map<String, Class<?>>> clientServiceClassMap = new ConcurrentHashMap<>();

    // applicationKey -> serviceId -> ClientServiceHandler
//    private final Map<String, Map<String, ClientServiceHandler>> clientServiceHandlerMap = new ConcurrentHashMap<>();

    private final Map<String, ClientServiceHandler> clientServiceHandlerMap = new ConcurrentHashMap<>();

    // ClientService Class Set
    private final Set<Class<?>> clientServiceClassSet = ConcurrentHashMap.newKeySet();

    private static final ClientServiceStorage INSTANCE = new ClientServiceStorage();

    public static ClientServiceStorage getInstance() {
        return INSTANCE;
    }

    private ClientServiceStorage() {
    }

    public ClientServiceStorage addClientServiceHandler(String serviceKey, ClientServiceHandler clientServiceHandler) {
        if (clientServiceHandlerMap.containsKey(serviceKey)) {
            throw new PanicException(STR."Client Service Handler of serviceKey [\{serviceKey}] has been existed !");
        }
        clientServiceHandlerMap.put(serviceKey, clientServiceHandler);
        return this;
    }

//    public ClientServiceStorage addClientServiceHandler(String applicationKey, String serviceId, ClientServiceHandler clientServiceHandler) {
//        Map<String, ClientServiceHandler> tempServiceHandlerMap = clientServiceHandlerMap.computeIfAbsent(applicationKey, _ -> new ConcurrentHashMap<>());
//        if (tempServiceHandlerMap.containsKey(serviceId)) {
//            throw new PanicException(STR."Client Service Handler of serviceId [\{serviceId}] of [\{applicationKey}] has been existed !");
//        }
//        tempServiceHandlerMap.put(serviceId, clientServiceHandler);
//        return this;
//    }

    public ClientServiceStorage addClientServiceClass(String applicationKey, String serviceId, Class<?> clientServiceClass) {
        Map<String, Class<?>> tempServiceClassMap = clientServiceClassMap.computeIfAbsent(applicationKey, _ -> new ConcurrentHashMap<>());
        if (tempServiceClassMap.containsKey(serviceId)) {
            throw new PanicException(STR."Client Service Class of serviceId [\{serviceId}] of [\{applicationKey}] has been existed !");
        }
        tempServiceClassMap.put(serviceId, clientServiceClass);
        if (clientServiceClassSet.contains(clientServiceClass)) {
            throw new PanicException(STR."Client Service Class of [\{clientServiceClass.getName()}] has been existed !");
        }
        clientServiceClassSet.add(clientServiceClass);
        return this;
    }

    public ClientServiceStorage addClientService(String applicationId, String serviceId, ClientService clientService) {
        if (StringUtils.isEmpty(applicationId)) {
            throw new PanicException("Application id is empty !");
        }
        if (StringUtils.isEmpty(serviceId)) {
            throw new PanicException("Service id is empty !");
        }
        Map<String, ClientService> tempServiceMap = clientServiceMap.computeIfAbsent(applicationId, _ -> new ConcurrentHashMap<>());
        if (tempServiceMap.containsKey(serviceId)) {
            throw new PanicException(STR."Client Service of serviceId [\{serviceId}] has been existed !");
        }
        try {
            for (ClientMethod clientMethod : clientService.getClientMethods()) {
                Method method;
                try {
                    method = Class.forName(clientService.getClientClassFullName())
                            .getMethod(clientMethod.getClientMethodName(), clientMethod.getArgTypes());
                } catch (NoSuchMethodException e) {
                    throw new PanicException(STR."Method of methodName: \{clientMethod.getClientMethodName()} and args: \{(new ArrayList<>(Arrays.asList(clientMethod.getArgTypes()))).toString()} not found !");

                }
                clientMethod.setParamTypes(Arrays.stream(method.getGenericParameterTypes()).toList())
                        .returnGenericType(method.getGenericReturnType());
                clientMethod.returnType(method.getReturnType());
            }
            clientService.settleClientMethod();
            tempServiceMap.put(serviceId, clientService);
            return this;
        } catch (ClassNotFoundException e) {
            throw new PanicException(STR."Class of \{clientService.getClientClassFullName()} not found !");
        }
    }

    public Map<String, ClientServiceHandler> getClientServiceHandlerMap(String applicationId) {
        return Collections.unmodifiableMap(clientServiceHandlerMap);
    }
}
