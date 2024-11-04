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

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A factory class that creates threads with a specified name prefix.
 */
public class NameThreadFactory implements ThreadFactory {

    // The prefix of the thread name
    private final String namePrefix;
    // The priority of the thread
    private final int priority;
    // Whether the thread is a daemon thread
    private final boolean daemon;

    private static final AtomicInteger POOL_NUMBER = new AtomicInteger(0);

    /**
     * Creates a new NameThreadFactory with the specified name prefix.
     * @param namePrefix the prefix of the thread name
     */
    public NameThreadFactory(String namePrefix) {
        this(namePrefix, Thread.NORM_PRIORITY, false);
    }

    /**
     * Creates a new NameThreadFactory with the specified name prefix, priority, and daemon flag.
     * @param namePrefix the prefix of the thread name
     * @param priority the priority of the thread
     * @param daemon whether the thread is a daemon thread
     */
    public NameThreadFactory(String namePrefix, int priority, boolean daemon) {
        this.namePrefix = namePrefix;
        this.priority = priority;
        this.daemon = daemon;
    }

    /**
     * Creates a new thread with the specified runnable.
     * @param r the runnable to be executed by the thread
     * @return the created thread
     */
    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, namePrefix + "-pool-thread-" + POOL_NUMBER.getAndIncrement());
        thread.setPriority(priority);
        thread.setDaemon(daemon);
        return thread;
    }
}
