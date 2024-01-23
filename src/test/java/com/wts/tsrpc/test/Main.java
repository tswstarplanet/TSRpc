package com.wts.tsrpc.test;

import com.wts.tsrpc.server.HttpServer;
import com.wts.tsrpc.server.HttpServerInitializer;
import com.wts.tsrpc.server.filter.CheckInvokerFilter;
import com.wts.tsrpc.server.filter.LogInvokerFilter;
import com.wts.tsrpc.server.manage.Application;
import com.wts.tsrpc.server.manage.Dispatcher;
import com.wts.tsrpc.server.manage.Manager;
import com.wts.tsrpc.server.service.GsonTransformer;
import com.wts.tsrpc.server.service.Service;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        manager.application(new Application()
                        .name("ServiceProvider")
                        .version("1.0"))
                .addService("complexService", (new Service())
                        .classFullName("com.wts.tsrpc.test.ProviderService")
                        .methodName("complexService")
                        .argTypes(new Class[]{Request2.class, String.class})
                        .returnType(Response.class))
                .dispatcher(new Dispatcher()
                        .manager(manager))
                .addServiceObj("complexService", new ProviderService())
                .addTransformer("gson", (new GsonTransformer())
                        .manager(manager))
                .serviceInvoker("reflect")
                .addInvokerFilter(new LogInvokerFilter())
                .addInvokerFilter(new CheckInvokerFilter());



        HttpServer httpServer = (new HttpServer())
                .port(8866)
                .bossNum(2)
                .workerNum(20)
                .serverInitializer((new HttpServerInitializer())
                        .manager(manager));
        httpServer.start();
    }
}