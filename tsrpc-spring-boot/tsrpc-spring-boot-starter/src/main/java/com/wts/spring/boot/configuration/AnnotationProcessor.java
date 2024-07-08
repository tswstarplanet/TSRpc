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

import com.wts.tsrpc.exception.SystemException;
import com.wts.tsrpc.spring.config.annotation.TSService;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

public class AnnotationProcessor implements ApplicationRunner, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (!(applicationContext instanceof ConfigurableApplicationContext)) {
            throw new SystemException("applicationContext must be ConfigurableApplicationContext");
        }
        ConfigurableApplicationContext genericApplicationContext = (ConfigurableApplicationContext) applicationContext;
        genericApplicationContext.getBeanFactory().registerSingleton("rpcServiceRegistry", new Object());

        applicationContext.getAutowireCapableBeanFactory()
        applicationContext.registerBean("rpcServiceRegistry", RpcServiceRegistry.class);

        // 获取所有带有@TSService注解的类
        String[] beanNames = applicationContext.getBeanNamesForAnnotation(TSService.class);
        for (String beanName : beanNames) {
            Object bean = applicationContext.getBean(beanName);
            // 获取类上的注解
            TSService rpcService = bean.getClass().getAnnotation(TSService.class);
            // 获取接口类
            Class<?>[] interfaces = bean.getClass().getInterfaces();
            if (interfaces.length == 0) {
                throw new RuntimeException("RpcService must implement at least one interface");
            }
            // 获取接口类
            Class<?> interfaceClass = interfaces[0];
            // 获取接口类的名称
            String interfaceName = interfaceClass.getName();
            // 获取版本号
            String version = rpcService.version();
            // 注册服务
            RpcServiceRegistry.register(interfaceName, version, bean);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
