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

import com.wts.tsrpc.client.ClientServiceStorage;
import com.wts.tsrpc.client.proxy.ClientServiceHandler;
import com.wts.tsrpc.client.service.ClientService;
import com.wts.tsrpc.client.utils.NameUtils;
import com.wts.tsrpc.common.utils.ReflectUtils;
import com.wts.tsrpc.server.manage.Application;
import com.wts.tsrpc.spring.config.annotation.TSClient;
import com.wts.tsrpc.spring.utils.AnnotationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.reflect.Proxy;
import java.util.Set;

public class TSClientRegistrar<T> implements ImportBeanDefinitionRegistrar {

    private static final Logger LOGGER = LoggerFactory.getLogger(TSClientRegistrar.class);

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // Scan all interfaces in the specified package, or get the package to scan based on the annotation metadata
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
            }
        };
        scanner.addIncludeFilter(new AnnotationTypeFilter(TSClient.class));

        Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents("com.wts");

        for (BeanDefinition candidate : candidateComponents) {
            String interfaceName = candidate.getBeanClassName();
            try {
                Class<T> interfaceType = (Class<T>) Class.forName(interfaceName);
                if (interfaceType.isInterface()) {
                    TSClient tsClient = AnnotationUtils.getAnnotationInfo(interfaceType, TSClient.class);

                    // 为接口创建代理类
                    Object proxyInstance = createProxy(interfaceType, tsClient);

                    // 将代理实例注册为 Spring Bean
                    registry.registerBeanDefinition(interfaceType.getName(), BeanDefinitionBuilder.genericBeanDefinition(interfaceType, () -> (T) proxyInstance).getBeanDefinition());
                }
            } catch (ClassNotFoundException e) {
                LOGGER.error("Failed to create proxy for interface: {}", interfaceName, e);
            }
        }
    }

    private T createProxy(Class<T> interfaceType, TSClient tsClient) {
        ClientServiceHandler clientServiceHandler = new ClientServiceHandler();
        clientServiceHandler.setInterfaceType(interfaceType);
        ClientService clientService = new ClientService();
        String applicationId = tsClient.applicationId();
        String applicationVersion = tsClient.applicationVersion();
        String serviceId = tsClient.serviceId();
        Application application = Application.getApplication(applicationId, applicationVersion);
        clientService.setServiceApplication(application);
        clientService.setServiceId(serviceId);
        clientService.setClientClassFullName(interfaceType.getName());
        clientService.setClientMethods(ReflectUtils.getClientMethods(interfaceType));
        clientServiceHandler.setClientService(clientService);
        ClientServiceStorage.getInstance().addClientServiceClass(NameUtils.applicationKey(applicationId, applicationVersion), serviceId, interfaceType)
                .addClientServiceHandler(NameUtils.serviceBeanName(applicationId, applicationVersion, serviceId), clientServiceHandler);
        return (T) Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class[]{interfaceType}, clientServiceHandler);
    }
}
