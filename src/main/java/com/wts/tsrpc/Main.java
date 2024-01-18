package com.wts.tsrpc;

import com.wts.tsrpc.manage.Application;
import com.wts.tsrpc.manage.Manager;
import com.wts.tsrpc.manage.ReflectDispatcher;
import com.wts.tsrpc.server.HttpServer;
import com.wts.tsrpc.server.HttpServerInitializer;
import com.wts.tsrpc.service.GsonTransformer;
import com.wts.tsrpc.service.Service;
import com.wts.tsrpc.test.ProviderService;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        manager.application(new Application()
                        .name("ServiceProvider")
                        .version("1.0"))
                .addService("primitiveService", (new Service())
                        .classFullName("com.wts.tsrpc.test.ProviderService")
                        .methodName("primitiveService")
                        .argTypes(new Class[]{String.class, Integer.class})
                        .returnType(String.class))
                .addDispatcher("reflect", (new ReflectDispatcher())
                        .manager(manager))
                .addServiceObj("primitiveService", new ProviderService())
                .addTransformer("gson", (new GsonTransformer())
                        .manager(manager));



        HttpServer httpServer = (new HttpServer())
                .port(8866)
                .bossNum(2)
                .workerNum(20)
                .serverInitializer((new HttpServerInitializer())
                        .manager(manager));
        httpServer.start();
    }
}