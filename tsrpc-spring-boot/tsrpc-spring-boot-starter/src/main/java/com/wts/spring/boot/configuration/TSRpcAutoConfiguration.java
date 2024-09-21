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
import com.wts.spring.boot.configuration.properties.ServerProperties;
import com.wts.tsrpc.common.transform.JacksonTransformer;
import com.wts.tsrpc.common.transform.Transformers;
import com.wts.tsrpc.server.manage.Application;
import com.wts.tsrpc.server.manage.ServerDispatcher;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ApplicationConfigurationProperties.class, ServerProperties.class})
public class TSRpcAutoConfiguration {

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
    public ServerDispatcher dispatcher(ServerProperties serverProperties) {
        return new ServerDispatcher(serverProperties.getServiceInvoker());
    }

    @Bean
    public Transformers transformers(ServerProperties serverProperties) {
        Transformers transformers = new Transformers();
        if ("jackson".equals(serverProperties.getTransformType())) {
            transformers.addTransformer("jackson", new JacksonTransformer());
        }
        // default use jackson
        else {
            transformers.addTransformer("jackson", new JacksonTransformer());
        }
        return transformers;
    }

    @Bean
    public TSRpcBeanPostProcessor tsRpcBeanPostProcessor() {
        return new TSRpcBeanPostProcessor();
    }


}
