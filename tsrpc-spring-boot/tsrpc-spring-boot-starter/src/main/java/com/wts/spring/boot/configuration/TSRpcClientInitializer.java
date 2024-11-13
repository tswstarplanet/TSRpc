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

package com.wts.spring.boot.configuration;

import com.wts.tsrpc.client.ClientDispatcher;
import com.wts.tsrpc.client.ClientEnd;
import com.wts.tsrpc.client.ClientInvoker;
import com.wts.tsrpc.client.ClientServiceStorage;
import com.wts.tsrpc.client.concurrent.ClientThreadPool;
import com.wts.tsrpc.client.loadbalance.LoadBalancer;
import com.wts.tsrpc.client.proxy.ClientServiceHandler;
import com.wts.tsrpc.common.proxy.ClassTool;
import com.wts.tsrpc.common.registry.Registry;
import com.wts.tsrpc.server.manage.Application;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

public class TSRpcClientInitializer implements SmartInitializingSingleton, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void afterSingletonsInstantiated() {
        initClient();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private void initClient() {
        Map<String, ClientServiceHandler> clientServiceHandlerMap = ClientServiceStorage.getInstance().getClientServiceHandlerMap();
        clientServiceHandlerMap.forEach((_, clientServiceHandler) -> {
            Class<?> interfaceType = clientServiceHandler.getInterfaceType();
            Application application = clientServiceHandler.getClientService().getServiceApplication();
            ClassTool classTool = new ClassTool();
            ClientDispatcher clientDispatcher = applicationContext.getBean("clientDispatcher", ClientDispatcher.class);
            ClientInvoker clientInvoker = new ClientInvoker();
            clientInvoker.clientService(clientServiceHandler.getClientService())
                    .loadBalancer(applicationContext.getBean("loadBalancer", LoadBalancer.class))
                    .clientDispatcher(applicationContext.getBean("clientDispatcher", ClientDispatcher.class))
                    .executorService(applicationContext.getBean("clientThreadPool", ClientThreadPool.class).getExecutorService())
                    .transformer(applicationContext.getBean("transformer", com.wts.tsrpc.common.Transformer.class));
            clientDispatcher.addClientInvoker(application.getKey(), clientServiceHandler.getClientService().getServiceId(), clientInvoker);

            classTool.clientDispatcher(clientDispatcher);
            Object target = classTool.getOrCreateClientServiceProxy(interfaceType, interfaceType.getDeclaredMethods(), application, clientServiceHandler.getClientService());
            clientServiceHandler.setTarget(target);

//            ClientInvoker clientInvoker = new ClientInvoker();
            ClientEnd clientEnd = applicationContext.getBean(ClientEnd.class);
            clientServiceHandler.getClientService().setTimeout(clientEnd.getTimeout());
//            clientInvoker.clientService(clientServiceHandler.getClientService())
//                    .loadBalancer(applicationContext.getBean("loadBalancer", LoadBalancer.class))
//                    .clientDispatcher(applicationContext.getBean("clientDispatcher", ClientDispatcher.class))
//                    .executorService(applicationContext.getBean("clientThreadPool", ClientThreadPool.class).getExecutorService());
//            clientDispatcher.addClientInvoker(application.getKey(), clientServiceHandler.getClientService().getServiceId(), clientInvoker);

            Registry registry = applicationContext.getBean(Registry.class);
            registry.subscribe(clientServiceHandler.getClientService().getServiceApplication());
        });
    }
}
