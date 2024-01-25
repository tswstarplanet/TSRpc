package com.wts.tsrpc.test;

import com.wts.tsrpc.server.HttpServer;
import com.wts.tsrpc.server.HttpServerInitializer;
import com.wts.tsrpc.server.filter.CheckInvokerFilter;
import com.wts.tsrpc.server.filter.LogInvokerFilter;
import com.wts.tsrpc.server.manage.Application;
import com.wts.tsrpc.server.manage.Dispatcher;
import com.wts.tsrpc.server.manage.Manager;
import com.wts.tsrpc.server.service.JsonTransformer;
import com.wts.tsrpc.server.service.Service;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        Service service = (new Service())
                .classFullName("com.wts.tsrpc.test.ProviderService")
                .methodName("complexService")
                .argTypes(new Class[]{Request.class, String.class})
                .returnType(Response.class);
        manager.application(new Application()
                        .name("ServiceProvider")
                        .version("1.0"))
                .addService("complexService", service)
                .dispatcher(new Dispatcher()
                        .manager(manager))
                .addServiceObj("complexService", new ProviderService())
                .addTransformer("gson", (new JsonTransformer())
                        .manager(manager))
                .serviceInvoker("reflect")
                .addInvokerFilter(new LogInvokerFilter())
                .addInvokerFilter(new CheckInvokerFilter())
                .addServiceParamType("complexService", service);



        HttpServer httpServer = (new HttpServer())
                .port(8866)
                .bossNum(2)
                .workerNum(20)
                .serverInitializer((new HttpServerInitializer())
                        .manager(manager));
        httpServer.start();
    }
}