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

import com.wts.tsrpc.common.ServiceResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ClientInvokerResponseCache {

    private static final Map<String, CompletableFuture<ServiceResponse>> pendingResponseMap = new java.util.concurrent.ConcurrentHashMap<>();

    private static final ClientInvokerResponseCache instance = new ClientInvokerResponseCache();

    private static final Map<String, Throwable> exceptionMap = new java.util.concurrent.ConcurrentHashMap<>();

    private ClientInvokerResponseCache() {
    }

    public static ClientInvokerResponseCache getInstance() {
        return instance;
    }

    public void putFuture(String requestId, CompletableFuture<ServiceResponse> responseFuture) {
        pendingResponseMap.put(requestId, responseFuture);
    }

    public CompletableFuture<ServiceResponse> getFuture(String requestId) {
        return pendingResponseMap.get(requestId);
    }

    public void removeFuture(String requestId) {
        pendingResponseMap.remove(requestId);
    }

    public void putException(String requestId, Throwable throwable) {
        exceptionMap.put(requestId, throwable);
    }

    public Throwable getException(String requestId) {
        return exceptionMap.get(requestId);
    }
}
