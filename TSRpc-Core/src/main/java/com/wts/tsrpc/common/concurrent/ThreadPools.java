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

package com.wts.tsrpc.common.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * A singleton class that manages thread pools.
 */
public class ThreadPools {

    private static final Logger logger = LoggerFactory.getLogger(ThreadPools.class);

    // Singleton instance of ThreadPools
    private static final ThreadPools INSTANCE = new ThreadPools();

    // Map to store thread pools by name
    private final Map<String, ExecutorService> THREAD_POOL_MAP = new ConcurrentHashMap<>();

    /**
     * Private constructor to prevent instantiation.
     */
    private ThreadPools() {}

    /**
     * Returns the singleton instance of ThreadPools.
     *
     * @return the singleton instance of ThreadPools
     */
    public static ThreadPools getInstance() {
        return INSTANCE;
    }

    /**
     * Creates a new thread pool or get the existing thread pool with the specified name and number of threads.
     *
     * @param poolName the name of the thread pool
     * @param fixedThreadNum the number of threads in the thread pool
     * @return the created thread pool
     */
    public ExecutorService getOrCreate(String poolName, Integer fixedThreadNum) {
        ExecutorService executorService = get(poolName);
        if (executorService != null) {
            return executorService;
        }
        return THREAD_POOL_MAP.computeIfAbsent(poolName, _ -> Executors.newFixedThreadPool(fixedThreadNum, new NameThreadFactory(poolName)));
    }

    public ExecutorService getOrCreate(String poolName, Integer coreSize, Integer maxSize, Integer queueSize) {
        ExecutorService executorService = get(poolName);
        if (executorService != null) {
            return executorService;
        }
        return THREAD_POOL_MAP.computeIfAbsent(poolName, _ -> new ThreadPoolExecutor(coreSize, maxSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(queueSize), new NameThreadFactory(poolName)));
    }

    /**
     * Returns the thread pool with the specified name.
     *
     * @param poolName the name of the thread pool
     * @return the thread pool with the specified name
     */
    public ExecutorService get(String poolName) {
        return THREAD_POOL_MAP.get(poolName);
    }

    /**
     * Destroys all thread pools.
     */
    public void destroy() {
        THREAD_POOL_MAP.forEach((poolName, executorService) -> {
            executorService.shutdown();
            logger.info("Thread pool {} has been destroyed.", poolName);
        });
    }
}
