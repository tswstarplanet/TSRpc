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

package com.wts.tsrpc.server.concurrent;

import com.wts.tsrpc.common.concurrent.ThreadPools;

import java.util.concurrent.ExecutorService;

public class ServerThreadPool {
    private Integer corePoolSize;
    private Integer maximumPoolSize;
    private Integer queueSize;
    private ExecutorService executorService;

    public ServerThreadPool(Integer corePoolSize, Integer maximumPoolSize, Integer queueSize) {
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.queueSize = queueSize;
        this.executorService = ThreadPools.getInstance().getOrCreate("ServerThreadPool", corePoolSize, maximumPoolSize, queueSize);
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
}