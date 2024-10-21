package com.wts.spring.boot.configuration;

import com.wts.tsrpc.client.Endpoint;
import com.wts.tsrpc.common.registry.Registry;
import com.wts.tsrpc.server.manage.Application;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class TSRpcInitializer implements SmartInitializingSingleton, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterSingletonsInstantiated() {
        initRegistry();
    }



    private void initRegistry() {
        Registry registry = applicationContext.getBean("registry", Registry.class);
        registry.register(applicationContext.getBean("application", Application.class), applicationContext.getBean("serverEndpoint", Endpoint.class));
    }


}