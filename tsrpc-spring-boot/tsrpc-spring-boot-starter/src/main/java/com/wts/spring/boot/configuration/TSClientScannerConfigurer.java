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

import com.wts.tsrpc.client.ClientInstance;
import com.wts.tsrpc.exception.PanicException;
import com.wts.tsrpc.spring.config.annotation.TSClient;
import com.wts.tsrpc.spring.utils.AnnotationUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

/**
 * TSClient scanner
 */
public class TSClientScannerConfigurer implements BeanDefinitionRegistryPostProcessor {

    private String basePackage;

    public TSClientScannerConfigurer(String basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(TSClient.class));

        for (BeanDefinition candidate : scanner.findCandidateComponents(basePackage)) {
            String beanClassName = candidate.getBeanClassName();
            try {
                Class<?> clazz = Class.forName(beanClassName);
                if (!clazz.isInterface()) {
                    throw new PanicException("TSClient must be an interface");
                }
                // Register ProxyFactoryBean
                TSClient tsClient = AnnotationUtils.getAnnotationInfo(clazz, TSClient.class);

                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(ClientInstance.class);
                builder.addConstructorArgValue(clazz);
                builder.addConstructorArgValue(tsClient.applicationId());
                builder.addConstructorArgValue(tsClient.applicationVersion());
                builder.addConstructorArgValue(tsClient.serviceId());
                registry.registerBeanDefinition(beanClassName, builder.getBeanDefinition());
            } catch (ClassNotFoundException e) {
                throw new PanicException(e);
            }
        }
    }

    @Override
    public void postProcessBeanFactory(org.springframework.beans.factory.config.ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 不需要实现
    }
}
