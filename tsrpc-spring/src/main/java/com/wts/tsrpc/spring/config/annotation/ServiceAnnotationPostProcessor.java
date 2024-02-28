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

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ServiceAnnotationPostProcessor implements BeanDefinitionRegistryPostProcessor,
        EnvironmentAware,
        ResourceLoaderAware,
        BeanClassLoaderAware,
        ApplicationContextAware,
        InitializingBean {

    private BeanDefinitionRegistry registry;

    protected final Set<String> packagesToScan;

    private Set<String> resolvedPackagesToScan;

    private Environment environment;

    private ResourceLoader resourceLoader;

    private static final List<Class<? extends Annotation>> serviceAnnotationTypes = Lists.newArrayList(TSService.class);

    public ServiceAnnotationPostProcessor(Set<String> packagesToScan) {
        this.packagesToScan = packagesToScan;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.resolvedPackagesToScan = resolvePackagesToScan(packagesToScan);
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        this.registry = registry;
        scanServiceBeans(resolvedPackagesToScan, registry);
    }

    private void scanServiceBeans(Set<String> packagesToScan, BeanDefinitionRegistry registry) {
        var scanner = new TsRpcClassPathBeanDefinitionScanner(registry, environment, resourceLoader);
        BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();
        scanner.setBeanNameGenerator(beanNameGenerator);
        serviceAnnotationTypes.forEach(serviceAnnotationType -> {
            scanner.addIncludeFilter(new AnnotationTypeFilter(serviceAnnotationType));
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    private Set<String> resolvePackagesToScan(Set<String> packagesToScan) {
        Set<String> resolvedPackagesToScan = new LinkedHashSet<>(packagesToScan.size());
        for (String packageToScan : packagesToScan) {
            if (StringUtils.isNotEmpty(packageToScan)) {
                String resolvedPackageToScan = environment.resolvePlaceholders(packageToScan.trim());
                resolvedPackagesToScan.add(resolvedPackageToScan);
            }
        }
        return resolvedPackagesToScan;
    }
}
