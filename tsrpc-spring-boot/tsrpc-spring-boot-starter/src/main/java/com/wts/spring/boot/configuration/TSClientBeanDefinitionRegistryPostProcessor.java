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

import com.wts.tsrpc.client.utils.NameUtils;
import com.wts.tsrpc.spring.TSClientProxyFactoryBean;
import com.wts.tsrpc.spring.config.annotation.TSClient;
import com.wts.tsrpc.spring.utils.AnnotationUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

/**
 * TSClient scanner
 */
public class TSClientBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    private String basePackage;

    public TSClientBeanDefinitionRegistryPostProcessor(String basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
            }
        };

        scanner.addIncludeFilter(new AnnotationTypeFilter(TSClient.class));

        for (BeanDefinition candidate : scanner.findCandidateComponents(basePackage)) {
            String interfaceName = candidate.getBeanClassName();
            try {
                Class<?> interfaceClass = Class.forName(interfaceName);
                TSClient tsClient = AnnotationUtils.getAnnotationInfo(interfaceClass, TSClient.class);
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(TSClientProxyFactoryBean.class);
                builder.addPropertyValue("interfaceType", interfaceClass);
                builder.addPropertyValue("applicationId", tsClient.applicationId());
                builder.addPropertyValue("applicationVersion", tsClient.applicationVersion());
                builder.addPropertyValue("serviceId", tsClient.serviceId());
                registry.registerBeanDefinition(NameUtils.serviceBeanName(tsClient.applicationId(), tsClient.applicationVersion(), tsClient.serviceId()), builder.getBeanDefinition());
            } catch (ClassNotFoundException e) {
                throw new BeansException("Cannot load class：" + interfaceName, e) {};
            }
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinitionRegistryPostProcessor.super.postProcessBeanFactory(beanFactory);
    }
}
