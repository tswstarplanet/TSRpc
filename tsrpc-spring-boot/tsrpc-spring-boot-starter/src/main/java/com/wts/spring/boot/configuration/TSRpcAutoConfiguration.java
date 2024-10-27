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
import com.wts.spring.boot.configuration.properties.CommonProperties;
import com.wts.spring.boot.configuration.properties.LoadBalancerProperties;
import com.wts.spring.boot.configuration.properties.NacosRegistryProperties;
import com.wts.spring.boot.configuration.properties.RegistryProperties;
import com.wts.spring.boot.configuration.properties.ServerProperties;
import com.wts.tsrpc.client.Endpoint;
import com.wts.tsrpc.client.loadbalance.LoadBalancer;
import com.wts.tsrpc.client.loadbalance.RandomLoadBalancer;
import com.wts.tsrpc.common.Transformer;
import com.wts.tsrpc.common.registry.NacosRegistry;
import com.wts.tsrpc.common.registry.Registry;
import com.wts.tsrpc.common.transform.JacksonTransformer;
import com.wts.tsrpc.common.utils.NetworkUtils;
import com.wts.tsrpc.exception.SystemException;
import com.wts.tsrpc.server.HttpServer;
import com.wts.tsrpc.server.HttpServerInitializer;
import com.wts.tsrpc.server.Server;
import com.wts.tsrpc.server.filter.ServerInvokerFilter;
import com.wts.tsrpc.server.manage.Application;
import com.wts.tsrpc.server.manage.ServiceDispatcher;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.InvocationTargetException;

@AutoConfiguration
@EnableConfigurationProperties({ApplicationConfigurationProperties.class, ServerProperties.class, RegistryProperties.class,
        NacosRegistryProperties.class, LoadBalancerProperties.class, CommonProperties.class})
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
    public ServiceDispatcher dispatcher(ServerProperties serverProperties) {
        return new ServiceDispatcher(serverProperties.getServiceInvoker());
    }

    @Bean
    public Transformer transformer(CommonProperties commonProperties) {
        Transformer transformer = null;
        if ("jackson".equals(commonProperties.getTransformType())) {
            transformer = new JacksonTransformer();
        } // other transformer will be implemented in the future
        return transformer;
    }

    @Bean
    public TSRpcBeanPostProcessor tsRpcBeanPostProcessor() {
        return new TSRpcBeanPostProcessor();
    }

    @Bean
    @ConditionalOnBean(Application.class)
    @ConditionalOnProperty(prefix = "tsrpc.registry", name = "name", havingValue = "nacos")
    public Registry registry(NacosRegistryProperties nacosRegistryProperties, ServerProperties serverProperties, Application application) {
        if (StringUtils.isEmpty(nacosRegistryProperties.getServerList())) {
            String errorMsg = "Nacos server list is null, please check the configuration";
            logger.error(errorMsg);
            throw new SystemException(errorMsg);
        }
        if (StringUtils.isEmpty(nacosRegistryProperties.getNamespace())) {
            String errorMsg = "Nacos namespace is null, please check the configuration";
            logger.error(errorMsg);
            throw new SystemException(errorMsg);
        }
        return new NacosRegistry(nacosRegistryProperties.getServerList(), nacosRegistryProperties.getNamespace());
    }

    @Bean
    @ConditionalOnProperty(prefix = "tsrpc.loadbalancer", name = "type", havingValue = "random")
    public LoadBalancer loadBalancer(Registry registry) {
        if (registry == null) {
            String errorMsg = "Registry is null, please check the configuration";
            logger.error(errorMsg);
            throw new SystemException(errorMsg);
        }
        return new RandomLoadBalancer(registry);
    }

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
                    throw new SystemException("Add invoker filter error", e);
                }
            });
        }
    }

    @Bean("server")
    public Server server(ServerProperties serverProperties, CommonProperties commonProperties, ServiceDispatcher serviceDispatcher, Transformer transformer) {
        Server server = null;
        if ("http".equals(commonProperties.getProtocol())) {
            Server httpServer = new HttpServer(serverProperties.getPort(), serverProperties.getBossNum(), serverProperties.getWorkerNum());
            HttpServerInitializer serverInitializer = (new HttpServerInitializer())
                    .serverDispatcher(serviceDispatcher)
                    .transformer(transformer);
        } // other server will be implemented in the future
        return server;
    }
}
