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

package com.wts.tsrpc.spring.config.annotation;

import com.wts.tsrpc.exception.ConfigMistakeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.springframework.beans.factory.support.BeanDefinitionBuilder.rootBeanDefinition;

public class TsRpcScanRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Set<String> packagesToScan = getPackagesToScan(importingClassMetadata);

        registerServiceAnnotationPostProcessor(packagesToScan, registry);
    }

    private Set<String> getPackagesToScan(AnnotationMetadata metadata) {
        // get from @EnableTsRpc
        Set<String> packagesToScan =
                getPackagesToScan0(metadata, EnableTsRpc.class, "basePackages");

        if (packagesToScan.isEmpty()) {
            return Collections.singleton(ClassUtils.getPackageName(metadata.getClassName()));
        }
        return packagesToScan;
    }

    /**
     * Registers {@link ServerPostProcessor}
     *
     * @param packagesToScan packages to scan without resolving placeholders
     * @param registry       {@link BeanDefinitionRegistry}
     * @since 2.5.8
     */
    private void registerServiceAnnotationPostProcessor(Set<String> packagesToScan, BeanDefinitionRegistry registry) {

        BeanDefinitionBuilder builder = rootBeanDefinition(ServerPostProcessor.class);
        builder.addConstructorArgValue(packagesToScan);
        builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
        BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinition, registry);
    }

    private Set<String> getPackagesToScan0(
            AnnotationMetadata metadata,
            Class annotationClass,
            String basePackagesName) {

        AnnotationAttributes attributes =
                AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(annotationClass.getName()));
        if (attributes == null) {
            return Collections.emptySet();
        }

        Set<String> packagesToScan = new LinkedHashSet<>();
        // basePackages
        String[] basePackages = attributes.getStringArray(basePackagesName);
        for (int i = 0; i < basePackages.length; i++) {
            for (int j = i + 1; j < basePackages.length; j++) {
                if (checkPackageContains(basePackages[i], basePackages[j])) {
                    throw new ConfigMistakeException("The packages to scan has relationships of containing each other !");
                }
            }
        }

        packagesToScan.addAll(Arrays.asList(basePackages));

        // value
        if (attributes.containsKey("value")) {
            String[] value = attributes.getStringArray("value");
            packagesToScan.addAll(Arrays.asList(value));
        }
        return packagesToScan;
    }

    private boolean checkPackageContains(String package1, String package2) {
        return StringUtils.isNoneEmpty(package1, package2) && (package1.startsWith(package2) || package2.startsWith(package1));
    }
}
