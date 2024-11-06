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

import com.wts.tsrpc.client.service.ClientMethod;
import com.wts.tsrpc.client.service.ClientService;
import com.wts.tsrpc.exception.PanicException;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientServiceStorage {

    private final Map<String, Map<String, ClientService>> clientServiceMap = new ConcurrentHashMap<>();

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

}
