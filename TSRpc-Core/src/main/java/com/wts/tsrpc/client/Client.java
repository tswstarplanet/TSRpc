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

import com.wts.tsrpc.common.ServiceRequest;
import com.wts.tsrpc.common.ServiceResponse;
import io.netty.channel.ChannelFuture;

import java.util.concurrent.CompletableFuture;

public interface Client {

    HttpClient init();

    ChannelFuture connect();

    ChannelFuture sendMsg(String msg, CompletableFuture<ServiceResponse> completableFuture);

    ChannelFuture sendRequest(ServiceRequest serviceRequest, CompletableFuture<ServiceResponse> completableFuture);
}
