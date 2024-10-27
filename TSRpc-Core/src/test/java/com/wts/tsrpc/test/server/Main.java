package com.wts.tsrpc.test.server;

import com.wts.tsrpc.client.Endpoint;
import com.wts.tsrpc.common.registry.NacosRegistry;
import com.wts.tsrpc.common.registry.Registry;
import com.wts.tsrpc.server.HttpServer;
import com.wts.tsrpc.server.HttpServerInitializer;
import com.wts.tsrpc.server.filter.CheckServerInvokerFilter;
import com.wts.tsrpc.server.filter.LogServerInvokerFilter;
import com.wts.tsrpc.server.manage.Application;
import com.wts.tsrpc.server.manage.Manager;
import com.wts.tsrpc.server.manage.ServiceDispatcher;
import com.wts.tsrpc.common.transform.JacksonTransformer;
import com.wts.tsrpc.server.service.Service;
import com.wts.tsrpc.server.service.ServiceMethod;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        Service service = (new Service())
                .classFullName("com.wts.tsrpc.test.server.ProviderService")
                .serviceId("complexService")
                .method(new ServiceMethod("complexService", new Class<?>[]{Request.class, String.class}))
                .method(new ServiceMethod("func", new Class<?>[]{List.class}));
//                .methodName("complexService")
//                .argTypes(new Class[]{Request.class, String.class})
//                .returnType(Response.class);
        Application application = new Application()
                .applicationId("ServiceProvider")
                .version("1.0");

        manager.application(application)
                .addService("complexService", service)
                .dispatcher(new ServiceDispatcher()
                        .manager(manager))
                .addServiceObj("complexService", new ProviderService())
                .addTransformer("jackson", (new JacksonTransformer())
                        .manager(manager))
                .serviceInvoker("javassist")
                .addServiceInvokerFilter(new LogServerInvokerFilter())
                .addServiceInvokerFilter(new CheckServerInvokerFilter());
//                .addServiceParamType("complexService", service);

        Registry registry = new NacosRegistry("127.0.0.1:8848", "test_namespace");
        registry.register(application, new Endpoint("127.0.0.1", 8866));

        HttpServer httpServer = (new HttpServer())
                .port(8866)
                .bossNum(2)
                .workerNum(20)
                .serverInitializer((new HttpServerInitializer())
                        .manager(manager));
        httpServer.start();
    }
}