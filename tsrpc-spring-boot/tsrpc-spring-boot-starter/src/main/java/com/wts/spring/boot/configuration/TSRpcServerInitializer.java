package com.wts.spring.boot.configuration;

import com.wts.tsrpc.common.concurrent.ThreadPools;
import com.wts.tsrpc.server.HttpServer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class TSRpcServerInitializer implements SmartInitializingSingleton, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterSingletonsInstantiated() {
        initServer();
    }

    private void initServer() {
        HttpServer httpServer = applicationContext.getBean("server", HttpServer.class);
        ThreadPools.getInstance().getOrCreate("tsrpc-server", 1).execute(httpServer::start);
    }

}
