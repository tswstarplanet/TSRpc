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

import com.wts.tsrpc.client.filter.ClientInvokerFilter;
import com.wts.tsrpc.client.service.ClientService;
import com.wts.tsrpc.exception.BizException;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientDispatcher {

    private final Map<String, Map<String, ClientInvoker>> clientInvokerMap = new ConcurrentHashMap<>();

    private final Map<String, Map<String, ClientService>> clientServiceMap = new ConcurrentHashMap<>();

    private final List<ClientInvokerFilter> defaultClientInvokerFilters = new ArrayList<>();

    public ClientDispatcher addClientInvoker(String applicationKey, String serviceId, ClientInvoker clientInvoker) {
        if (StringUtils.isEmpty(applicationKey)) {
            throw new BizException("Application id is empty !");
        }
        if (StringUtils.isEmpty(serviceId)) {
            throw new BizException("Service id is empty !");
        }
        Map<String, ClientInvoker> tempInvokerMap = clientInvokerMap.computeIfAbsent(applicationKey, _ -> new ConcurrentHashMap<>());
        if (tempInvokerMap.containsKey(serviceId)) {
            throw new BizException(STR."Client Invoker of serviceId [\{serviceId}] has been existed !");
        }
        tempInvokerMap.putIfAbsent(serviceId, clientInvoker);
        return this;
    }

    public ClientInvoker getClientInvoker(String applicationKey, String serviceId) {
        return clientInvokerMap.get(applicationKey).get(serviceId);
    }

    public ClientService getClientService(String applicationId, String serviceId) {
        return clientServiceMap.get(applicationId).get(serviceId);
    }

    public ClientDispatcher addClientInvokerFilter(ClientInvokerFilter invokerFilter) {
        defaultClientInvokerFilters.add(invokerFilter);
        return this;
    }

    public List<ClientInvokerFilter> getDefaultClientInvokerFilters() {
        return List.copyOf(defaultClientInvokerFilters);
    }
}
