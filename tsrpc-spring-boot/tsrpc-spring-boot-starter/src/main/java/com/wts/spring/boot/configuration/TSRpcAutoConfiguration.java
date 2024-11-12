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

import com.wts.spring.boot.configuration.properties.ApplicationConfigurationProperties;
import com.wts.spring.boot.configuration.properties.ClientCommonProperties;
import com.wts.spring.boot.configuration.properties.ClientThreadPoolProperties;
import com.wts.spring.boot.configuration.properties.CommonProperties;
import com.wts.spring.boot.configuration.properties.RegistryProperties;
import com.wts.spring.boot.configuration.properties.ServerProperties;
import com.wts.tsrpc.client.ClientDispatcher;
import com.wts.tsrpc.client.ClientEnd;
import com.wts.tsrpc.client.Endpoint;
import com.wts.tsrpc.client.concurrent.ClientThreadPool;
import com.wts.tsrpc.client.filter.ClientInvokerFilter;
import com.wts.tsrpc.client.loadbalance.LoadBalancer;
import com.wts.tsrpc.client.loadbalance.RandomLoadBalancer;
import com.wts.tsrpc.common.Transformer;
import com.wts.tsrpc.common.registry.NacosRegistry;
import com.wts.tsrpc.common.registry.Registry;
import com.wts.tsrpc.common.transform.JacksonTransformer;
import com.wts.tsrpc.common.utils.NetworkUtils;
import com.wts.tsrpc.exception.PanicException;
import com.wts.tsrpc.server.HttpServer;
import com.wts.tsrpc.server.Server;
import com.wts.tsrpc.server.filter.ServerInvokerFilter;
import com.wts.tsrpc.server.manage.Application;
import com.wts.tsrpc.server.manage.ServiceDispatcher;
import com.wts.tsrpc.spring.config.ThreadPoolsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;

import java.lang.reflect.InvocationTargetException;

@AutoConfiguration
@Import(TSClientConfiguration.class)
@EnableConfigurationProperties({ApplicationConfigurationProperties.class, ServerProperties.class, RegistryProperties.class,
        ClientCommonProperties.class, CommonProperties.class, ClientThreadPoolProperties.class})
public class TSRpcAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(TSRpcAutoConfiguration.class);

//    @Bean
//    @ConditionalOnMissingBean
//    public Manager manager() {
//        return new Manager();
//    }
    @Bean
    public Application application(ApplicationConfigurationProperties applicationConfigurationProperties) {
        return new Application(applicationConfigurationProperties.getApplicationId(), applicationConfigurationProperties.getVersion());
    }

    @Bean
    public Transformer transformer(CommonProperties commonProperties, ServiceDispatcher serviceDispatcher) {
        Transformer transformer = null;
        if ("jackson".equals(commonProperties.getTransformType())) {
            transformer = new JacksonTransformer(serviceDispatcher, null);
        } // todo other transformer will be implemented in the future
        return transformer;
    }

    @Bean
    @DependsOn("serviceDispatcher")
    public TSRpcBeanPostProcessor tsRpcBeanPostProcessor() {
        return new TSRpcBeanPostProcessor();
    }

//    @Bean
//    @ConditionalOnBean(Application.class)
//    @ConditionalOnProperty(prefix = "tsrpc.registry", name = "name", havingValue = "nacos")
//    public Registry registry(RegistryProperties registryProperties) {
//        if (StringUtils.isEmpty(registryProperties.getServerList())) {
//            String errorMsg = "Nacos server list is null, please check the configuration";
//            logger.error(errorMsg);
//            throw new SystemException(errorMsg);
//        }
//        if (StringUtils.isEmpty(registryProperties.getNamespace())) {
//            String errorMsg = "Nacos namespace is null, please check the configuration";
//            logger.error(errorMsg);
//            throw new SystemException(errorMsg);
//        }
//        return new NacosRegistry(registryProperties.getServerList(), registryProperties.getNamespace());
//    }

//    @Bean
//    @ConditionalOnProperty(prefix = "tsrpc.loadbalancer", name = "type", havingValue = "random")
//    public LoadBalancer loadBalancer(Registry registry) {
//        if (registry == null) {
//            String errorMsg = "Registry is null, please check the configuration";
//            logger.error(errorMsg);
//            throw new PanicException(errorMsg);
//        }
//        return new RandomLoadBalancer(registry);
//    }

    @Bean("serverEndpoint")
    public Endpoint endpoint(ServerProperties serverProperties) {
        return new Endpoint(NetworkUtils.getLocalHost(), serverProperties.getPort());
    }

    @Bean("serviceDispatcher")
    public ServiceDispatcher serviceDispatcher(ServerProperties serverProperties) {
        ServiceDispatcher serviceDispatcher = new ServiceDispatcher(serverProperties.getServiceInvoker());
        if (serverProperties.getInvokerFilters() != null) {
            serverProperties.getInvokerFilters().forEach(filter -> {
                try {
                    serviceDispatcher.addServiceInvokerFilter((ServerInvokerFilter) Class.forName(filter).getDeclaredConstructor().newInstance());
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
                    logger.error("Add invoker filter error", e);
                    throw new PanicException("Add invoker filter error", e);
                }
            });
        }
        return serviceDispatcher;
    }

    @Bean("clientDispatcher")
    public ClientDispatcher clientDispatcher(ClientCommonProperties clientCommonProperties) {
        ClientDispatcher clientDispatcher = new ClientDispatcher();
        if (clientCommonProperties.getInvokerFilters() != null) {
            clientCommonProperties.getInvokerFilters().forEach(filter -> {
                try {
                    clientDispatcher.addClientInvokerFilter((ClientInvokerFilter) Class.forName(filter).getDeclaredConstructor().newInstance());
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
                    logger.error("Add invoker filter error", e);
                    throw new PanicException("Add invoker filter error", e);
                }
            });
        }
        return clientDispatcher;
    }

    @Bean("server")
    public Server server(ServerProperties serverProperties, CommonProperties commonProperties, ServiceDispatcher serviceDispatcher, Transformer transformer) {
        Server server = null;
        if ("http".equals(commonProperties.getProtocol())) {
            server = new HttpServer(serverProperties.getPort(), serverProperties.getBossNum(), serverProperties.getWorkerNum());
            server.init(serviceDispatcher, transformer);
        } // todo other server will be implemented in the future
        return server;
    }

    @Bean("registry")
    public Registry registry(RegistryProperties registryProperties) {
        Registry registry = null;
        if (registryProperties.isEnable()) {
            if ("nacos".equals(registryProperties.getName())) {
                registry = new NacosRegistry(registryProperties.getServerList(), registryProperties.getNamespace());
            } // todo other registry will be implemented in the future
        }
        return registry;
    }

    @Bean("threadPoolConfig")
    public ThreadPoolsConfig threadPoolsConfig() {
        return new ThreadPoolsConfig();
    }

    @Bean("TSRpcServerInitializer")
    public TSRpcServerInitializer tsRpcInitializer() {
        return new TSRpcServerInitializer();
    }

    @Bean("TSRpcRegistryInitializer")
    public TSRpcRegistryInitializer tsRpcRegistryInitializer() {
        return new TSRpcRegistryInitializer();
    }

    @Bean("loadBalancer")
    public LoadBalancer loadBalancer(ClientCommonProperties clientCommonProperties, Registry registry) {
        LoadBalancer loadBalancer = null;
        if ("random".equals(clientCommonProperties.getBalancerType())) {
            loadBalancer = new RandomLoadBalancer(registry);
        } // Todo other loadBalancer will be implemented later
        return loadBalancer;
    }

//    @Bean("clientServiceStorage")
//    public ClientServiceStorage clientServiceStorage() {
//        return new ClientServiceStorage();
//    }

//    @Bean("tsClientBeanDefinitionRegistryPostProcessor")
    public TSClientBeanDefinitionRegistryPostProcessor tsClientBeanDefinitionRegistryPostProcessor() {
        return new TSClientBeanDefinitionRegistryPostProcessor();
    }

    @Bean("clientThreadPool")
    public ClientThreadPool clientThreadPool(ClientThreadPoolProperties clientThreadPoolProperties) {
        return new ClientThreadPool(clientThreadPoolProperties.getCorePoolSize(), clientThreadPoolProperties.getMaxPoolSize(), clientThreadPoolProperties.getQueueSize());
    }

    @Bean("clientEnd")
    public ClientEnd clientEnd(ClientCommonProperties clientCommonProperties) {
        return new ClientEnd(clientCommonProperties.getTimeout());
    }

    @Bean("TSRpcClientInitializer")
    public TSRpcClientInitializer tsRpcClientInitializer() {
        return new TSRpcClientInitializer();
    }
}
