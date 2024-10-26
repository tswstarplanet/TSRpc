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

import com.wts.tsrpc.server.service.Service;
import com.wts.tsrpc.spring.config.annotation.TSClient;
import com.wts.tsrpc.spring.config.annotation.TSService;
import com.wts.tsrpc.spring.utils.AnnotationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class TSRpcBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!AnnotationUtils.isAnnotatedWith(bean.getClass(), TSService.class) || !AnnotationUtils.isAnnotatedWith(bean.getClass(), TSClient.class)) {
            return bean;
        }
        if (AnnotationUtils.isAnnotatedWith(bean.getClass(), TSService.class)) {
            TSService tsService = AnnotationUtils.getAnnotationInfo(bean.getClass(), TSService.class);
            Service service = new Service();
            service.classFullName(bean.getClass().getName());
            service.serviceId(StringUtils.isEmpty(tsService.serviceId()) ? bean.getClass().getName() : tsService.serviceId());
            if (tsService.exportAllPublicMethods()) {

            }
//                    .classFullName("com.wts.tsrpc.test.server.ProviderService")
//                    .serviceId("complexService")
//                    .method(new ServiceMethod("complexService", new Class<?>[]{Request.class, String.class}))
//                    .method(new ServiceMethod("func", new Class<?>[]{List.class}));

        }
        if (AnnotationUtils.isAnnotatedWith(bean.getClass(), TSClient.class)) {

        }
        return bean;
    }
}
