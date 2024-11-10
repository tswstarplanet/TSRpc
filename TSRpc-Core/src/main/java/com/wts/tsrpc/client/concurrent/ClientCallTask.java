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

package com.wts.tsrpc.client.concurrent;

import com.wts.tsrpc.client.filter.ClientInvokerFilterChain;
import com.wts.tsrpc.common.ServiceRequest;
import com.wts.tsrpc.common.ServiceResponse;
import com.wts.tsrpc.exception.PanicException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ClientCallTask implements Callable<ServiceResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientCallTask.class);

    private ClientInvokerFilterChain filterChain;

    ServiceRequest serviceRequest;

    private Long timeout;

    public ClientCallTask(ClientInvokerFilterChain filterChain, ServiceRequest serviceRequest, Long timeout) {
        this.filterChain = filterChain;
        this.serviceRequest = serviceRequest;
        this.timeout = timeout;
    }

    @Override
    public ServiceResponse call() throws Exception {
        CompletableFuture<ServiceResponse> responseFuture = new CompletableFuture<>();
        try {
            filterChain.doFilter(serviceRequest, filterChain, responseFuture);
            return responseFuture.get(timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            LOGGER.error("Client call task error !", e);
            throw new PanicException("Client call task error !");
        }
    }
}
