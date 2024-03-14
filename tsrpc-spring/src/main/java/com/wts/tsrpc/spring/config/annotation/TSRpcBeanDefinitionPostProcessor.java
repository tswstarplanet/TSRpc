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
import com.wts.tsrpc.common.PropertySourceConfigConstants;
import com.wts.tsrpc.common.registry.RegistryFactory;
import com.wts.tsrpc.common.utils.ReflectUtils;
import com.wts.tsrpc.exception.ConfigMistakeException;
import com.wts.tsrpc.exception.SystemException;
import com.wts.tsrpc.server.manage.Application;
import com.wts.tsrpc.server.manage.Manager;
import com.wts.tsrpc.server.service.Service;
import com.wts.tsrpc.server.service.ServiceMethod;
import com.wts.tsrpc.spring.config.annotation.utils.AnnotationUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static org.springframework.beans.factory.support.BeanDefinitionBuilder.rootBeanDefinition;
import static org.springframework.util.ClassUtils.resolveClassName;

public class TSRpcBeanDefinitionPostProcessor implements BeanDefinitionRegistryPostProcessor,
        EnvironmentAware,
        ResourceLoaderAware,
        BeanClassLoaderAware,
        ApplicationContextAware,
        InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(TSRpcBeanDefinitionPostProcessor.class);

    private BeanDefinitionRegistry registry;

    protected final Set<String> packagesToScan;

    private Set<String> resolvedPackagesToScan;

    private Environment environment;

    private ResourceLoader resourceLoader;

    private ClassLoader classLoader;

    private ApplicationContext applicationContext;

    private static final List<Class<? extends Annotation>> serviceAnnotationTypes = Lists.newArrayList(TSService.class);

    public TSRpcBeanDefinitionPostProcessor(Set<String> packagesToScan) {
        this.packagesToScan = packagesToScan;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.resolvedPackagesToScan = resolvePackagesToScan(packagesToScan);
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        this.registry = registry;
        registerCommonBean();
        scanServiceBeans(resolvedPackagesToScan, registry);
    }

    private void registerCommonBean() {
        registerManager();
        registerApplication();
        registerRegistry();
    }

    private void registerManager() {
        BeanDefinitionBuilder builder = rootBeanDefinition(Manager.class);
        builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
        BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinition, registry);
    }

    /**
     * Registry application bean.
     */
    private void registerApplication() {
        BeanDefinitionBuilder builder = rootBeanDefinition(Application.class);
        builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        String applicationId = environment.getProperty(PropertySourceConfigConstants.APPLICATION_ID);
        if (StringUtils.isEmpty(applicationId)) {
            logger.error("No config of application id !");
            throw new ConfigMistakeException("No config of application id !");
        }
        builder.addPropertyValue("applicationId", applicationId);
        builder.addPropertyValue("version", environment.getProperty(PropertySourceConfigConstants.APPLICATION_VERSION, PropertySourceConfigConstants.DEFAULT_APPLICATION_VERSION));
        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
        BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinition, registry);
    }

    private void registerRegistry() {
        String registryType = environment.getProperty(PropertySourceConfigConstants.REGISTRY_TYPE, PropertySourceConfigConstants.REGISTRY_TYPE_NACOS);
        Class<?> registryClass = RegistryFactory.getRegistryClass(registryType);
        BeanDefinitionBuilder builder = rootBeanDefinition(registryClass);
        builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        String namespace = environment.getProperty(PropertySourceConfigConstants.REGISTRY_NAMESPACE, PropertySourceConfigConstants.DEFAULT_NAMESPACE);
        String serverList = environment.getProperty(PropertySourceConfigConstants.REGISTRY_SERVER_LIST);
        if (StringUtils.isEmpty(serverList)) {
            throw new ConfigMistakeException("No server list config of registry !");
        }
        builder.addConstructorArgValue(serverList);
        builder.addConstructorArgValue(namespace);
        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
        BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinition, registry);
    }



//    @Override
//    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
//        if (this.registry == null) {
//            // In spring 3.x, may be not call postProcessBeanDefinitionRegistry()
//            this.registry = (BeanDefinitionRegistry) beanFactory;
//        }
//
//        // scan bean definitions
//        String[] beanNames = beanFactory.getBeanDefinitionNames();
//        for (String beanName : beanNames) {
//            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
//            Map<String, Object> annotationAttributes = getServiceAnnotationAttributes(beanDefinition);
//            if (annotationAttributes != null) {
//                // process @DubboService at java-config @bean method
//                processAnnotatedBeanDefinition(
//                        beanName, (AnnotatedBeanDefinition) beanDefinition, annotationAttributes);
//            }
//        }
//
//        if (!scanned) {
//            // In spring 3.x, may be not call postProcessBeanDefinitionRegistry(), so scan service class here
//            scanServiceBeans(resolvedPackagesToScan, registry);
//        }
//    }

//    /**
//     * Get dubbo service annotation class at java-config @bean method
//     * @return return service annotation attributes map if found, or return null if not found.
//     */
//    private Map<String, Object> getServiceAnnotationAttributes(BeanDefinition beanDefinition) {
//        if (beanDefinition instanceof AnnotatedBeanDefinition) {
//            AnnotatedBeanDefinition annotatedBeanDefinition = (AnnotatedBeanDefinition) beanDefinition;
//            MethodMetadata factoryMethodMetadata = SpringCompatUtils.getFactoryMethodMetadata(annotatedBeanDefinition);
//            if (factoryMethodMetadata != null) {
//                // try all dubbo service annotation types
//                for (Class<? extends Annotation> annotationType : serviceAnnotationTypes) {
//                    if (factoryMethodMetadata.isAnnotated(annotationType.getName())) {
//                        // Since Spring 5.2
//                        // return
//                        // factoryMethodMetadata.getAnnotations().get(annotationType).filterDefaultValues().asMap();
//                        // Compatible with Spring 4.x
//                        Map<String, Object> annotationAttributes =
//                                factoryMethodMetadata.getAnnotationAttributes(annotationType.getName());
//                        return filterDefaultValues(annotationType, annotationAttributes);
//                    }
//                }
//            }
//        }
//        return null;
//    }

    private void scanServiceBeans(Set<String> packagesToScan, BeanDefinitionRegistry registry) {
        var scanner = new TSRpcBeanDefinitionScanner(registry, environment, resourceLoader);
        BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();
        scanner.setBeanNameGenerator(beanNameGenerator);
        serviceAnnotationTypes.forEach(serviceAnnotationType -> {
            scanner.addIncludeFilter(new AnnotationTypeFilter(serviceAnnotationType));
        });
        packagesToScan.forEach(packageToScan -> {
            scanner.scan(packageToScan);
            Set<BeanDefinitionHolder> beanDefinitionHolders = findServiceBeanDefinitionHolders(scanner, packageToScan, registry, beanNameGenerator);
            if (!CollectionUtils.isEmpty(beanDefinitionHolders)) {
                if (logger.isInfoEnabled()) {
                    List<String> serviceClasses = new ArrayList<>(beanDefinitionHolders.size());
                    for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders) {
                        serviceClasses.add(
                                beanDefinitionHolder.getBeanDefinition().getBeanClassName());
                    }
                    logger.info("Found " + beanDefinitionHolders.size()
                            + " classes annotated by Dubbo @Service under package [" + packageToScan + "]: "
                            + serviceClasses);
                }

                for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitionHolders) {
                    processScannedBeanDefinition(beanDefinitionHolder);
//                    servicePackagesHolder.addScannedClass(
//                            beanDefinitionHolder.getBeanDefinition().getBeanClassName());
                }
            } else {
                if (logger.isWarnEnabled()) {
                    logger.warn(
                            "No annotations were found on the class",
                            "",
                            "No class annotated by Dubbo @DubboService or @Service was found under package ["
                                    + packageToScan + "], ignore re-scanned classes: ");
                }
            }
        });
    }

    /**
     * Registers {@link } from new annotated {@link Service} {@link BeanDefinition}
     *
     * @param beanDefinitionHolder
     * @see
     * @see BeanDefinition
     */
    private void processScannedBeanDefinition(BeanDefinitionHolder beanDefinitionHolder) {

        Class<?> beanClass = resolveClass(beanDefinitionHolder);

        Annotation service = findServiceAnnotation(beanClass);

        // The attributes of @Service annotation
        Map<String, Object> serviceAnnotationAttributes = AnnotationUtils.getAttributes(service, true);

//        String serviceInterface = resolveInterfaceName(serviceAnnotationAttributes, beanClass);

        String annotatedServiceBeanName = beanDefinitionHolder.getBeanName();

        // ServiceBean Bean name
        String beanName = generateServiceBeanName(service, beanClass);

        AbstractBeanDefinition serviceBeanDefinition =
                buildServiceBeanDefinition(serviceAnnotationAttributes, beanDefinitionHolder.getBeanDefinition(), annotatedServiceBeanName);

        registerServiceBeanDefinition(beanName, serviceBeanDefinition);
    }

    private void registerServiceBeanDefinition(
            String serviceBeanName, AbstractBeanDefinition serviceBeanDefinition) {
        // check service bean
        if (registry.containsBeanDefinition(serviceBeanName)) {
            BeanDefinition existingDefinition = registry.getBeanDefinition(serviceBeanName);
            if (existingDefinition.equals(serviceBeanDefinition)) {
                // exist equipment bean definition
                return;
            }

            String msg = STR."Found duplicated BeanDefinition of service with bean name [\{serviceBeanName}], existing definition [\{existingDefinition}], new definition [\{serviceBeanDefinition}]";
            logger.error(msg);
            throw new BeanDefinitionStoreException(
                    serviceBeanDefinition.getResourceDescription(), serviceBeanName, msg);
        }

        registry.registerBeanDefinition(serviceBeanName, serviceBeanDefinition);
        if (logger.isInfoEnabled()) {
            logger.info("Register ServiceBean[" + serviceBeanName + "]: " + serviceBeanDefinition);
        }
    }

    /**
     * Build the {@link AbstractBeanDefinition Bean Definition}
     *
     *
     * @param serviceAnnotationAttributes
     * @param serviceInterface
     * @param annotatedServiceBeanName
     * @return
     * @since 2.7.3
     */
    private AbstractBeanDefinition buildServiceBeanDefinition(
            Map<String, Object> serviceAnnotationAttributes, BeanDefinition originBeanDefinition, String annotatedServiceBeanName) {

        BeanDefinitionBuilder builder = rootBeanDefinition(Service.class);

        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
        beanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);

        MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();

        String[] ignoreAttributeNames = {};

        propertyValues.addPropertyValues(
                new AnnotationPropertyValuesAdapter(serviceAnnotationAttributes, environment, ignoreAttributeNames));

        // Set serviceId
        builder.addPropertyValue("serviceId", annotatedServiceBeanName);

        builder.addPropertyValue("classFullName", originBeanDefinition.getBeanClassName());

        // Convert methods into map
        builder.addPropertyValue("methods", getServiceMethods(serviceAnnotationAttributes, originBeanDefinition));

        return builder.getBeanDefinition();
    }

    private List<ServiceMethod> getServiceMethods(Map<String, Object> serviceAnnotationAttributes, BeanDefinition originBeanDefinition) {
        try {
            Class<?> clazz = Class.forName(originBeanDefinition.getBeanClassName());
            if ((boolean) serviceAnnotationAttributes.get("exportAllMethods")) {
                List<Method> publicMethods = ReflectUtils.getPublicDeclaredMethods(clazz);
                return publicMethods.stream().map(method ->
                        (new ServiceMethod()).methodName(method.getName())
                                .argTypes(method.getParameterTypes())
                                .parameterTypes(method.getGenericParameterTypes())
                                .returnType(method.getReturnType())).toList();
            }
            return Lists.newArrayList();
        } catch (ClassNotFoundException e) {
            throw new SystemException(e.getMessage());
        }
    }

    /**
     * Generates the bean name of
     *
     * @return ServiceBean@interfaceClassName#annotatedServiceBeanName
     * @since 2.7.3
     */
    private String generateServiceBeanName(Annotation annotation, Class<?> clazz) {
        StringBuilder builder = new StringBuilder();
        switch (annotation) {
            case TSService ignored -> builder.append(STR."TSService$.\{clazz.getName()}$}");
            case TSClient ignored -> builder.append(STR."TSClient$.\{clazz.getName()}");
            default -> throw new IllegalStateException("Unexpected value: " + annotation);
        }
        return builder.toString();
    }

    /**
     * Find the {@link Annotation annotation} of @Service
     *
     * @param beanClass the {@link Class class} of Bean
     * @return <code>null</code> if not found
     * @since 2.7.3
     */
    private Annotation findServiceAnnotation(Class<?> beanClass) {
        return serviceAnnotationTypes.stream()
                .map(annotationType ->  AnnotatedElementUtils.findMergedAnnotation(
                        beanClass, annotationType))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private Class<?> resolveClass(BeanDefinitionHolder beanDefinitionHolder) {

        BeanDefinition beanDefinition = beanDefinitionHolder.getBeanDefinition();

        return resolveClass(beanDefinition);
    }

    private Class<?> resolveClass(BeanDefinition beanDefinition) {

        String beanClassName = beanDefinition.getBeanClassName();

        return resolveClassName(beanClassName, classLoader);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
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

    /**
     * Finds a {@link Set} of {@link BeanDefinitionHolder BeanDefinitionHolders} whose bean type annotated
     * {@link Service} Annotation.
     *
     * @param scanner       {@link ClassPathBeanDefinitionScanner}
     * @param packageToScan pachage to scan
     * @param registry      {@link BeanDefinitionRegistry}
     * @return non-null
     * @since 2.5.8
     */
    private Set<BeanDefinitionHolder> findServiceBeanDefinitionHolders(
            ClassPathBeanDefinitionScanner scanner,
            String packageToScan,
            BeanDefinitionRegistry registry,
            BeanNameGenerator beanNameGenerator) {

        Set<BeanDefinition> beanDefinitions = scanner.findCandidateComponents(packageToScan);

        Set<BeanDefinitionHolder> beanDefinitionHolders = new LinkedHashSet<>(beanDefinitions.size());

        for (BeanDefinition beanDefinition : beanDefinitions) {

            String beanName = beanNameGenerator.generateBeanName(beanDefinition, registry);
            BeanDefinitionHolder beanDefinitionHolder = new BeanDefinitionHolder(beanDefinition, beanName);
            beanDefinitionHolders.add(beanDefinitionHolder);
        }

        return beanDefinitionHolders;
    }
}
