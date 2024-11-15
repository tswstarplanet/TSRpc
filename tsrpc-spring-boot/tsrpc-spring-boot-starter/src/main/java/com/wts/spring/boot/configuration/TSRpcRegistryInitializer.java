package com.wts.spring.boot.configuration;

import com.wts.tsrpc.common.registry.Registry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class TSRpcRegistryInitializer implements SmartInitializingSingleton, ApplicationContextAware {

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
        registry.init();
        registry.register();
    }

}
