package com.wts.tsrpc.test.client;

import com.wts.tsrpc.client.ClientInvoker;
import com.wts.tsrpc.client.ClientService;
import com.wts.tsrpc.client.RandomLoadBalancer;
import com.wts.tsrpc.client.filter.CheckClientInvokerFilter;
import com.wts.tsrpc.client.filter.LogClientInvokerFilter;
import com.wts.tsrpc.common.proxy.ClassTool;
import com.wts.tsrpc.common.utils.JacksonUtils;
import com.wts.tsrpc.server.manage.Application;
import com.wts.tsrpc.server.manage.Manager;
import com.wts.tsrpc.server.service.JacksonTransformer;
import com.wts.tsrpc.test.server.*;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws NoSuchMethodException {
        Manager manager = new Manager();
//        HttpClient client = (new HttpClient("localhost", 8866, 10))
//                .manager(manager);
        ClientService clientService = new ClientService();
        Application application = (new Application())
                .name("ServerApplication1")
                .version("1.0");
        clientService.setServiceId("complexService");
        clientService.setApplicationId(application.getName());
        clientService.setApplicationVersion(application.getVersion());
        clientService.setArgTypes(new Class[]{Request.class, String.class});
        clientService.setClientClassFullName("com.wts.tsrpc.test.client.IProviderService");
        clientService.setClientMethodName("complexService");
        clientService.setTransformType("jackson");
        manager.addClientService("ServerApplication1", "providerService", clientService)
                .addTransformer("jackson", new JacksonTransformer());
        ClientInvoker clientInvoker = new ClientInvoker();
        clientInvoker.clientService(clientService)
                .manager(manager)
                .loadBalancer(new RandomLoadBalancer());
        manager.addClientInvoker(application.getKey(), clientService.getServiceId(), clientInvoker);
        manager.addClientInvokerFilter(new LogClientInvokerFilter())
                .addClientInvokerFilter(new CheckClientInvokerFilter());

        ClassTool classTool = new ClassTool();
        classTool.manager(manager);
        IProviderService iProviderService = (IProviderService) classTool
                .createClientServiceProxy(IProviderService.class, IProviderService.class.getDeclaredMethods(), application, clientService);

        Request<RequestBody<SubRequestBody, SubRequestBody>> request = new Request<>();
        request.setList(new ArrayList<>(Arrays.asList(new Test("a", 1), new Test("b", 2))));
        request.setReqId("REQ00001");
        request.setCount(1L);
        RequestBody<SubRequestBody, SubRequestBody> body = new RequestBody<>();
        body.setCode("REQ00002");
        body.setMsg("请求00001");
        request.setBody(body);
        SubRequestBody subRequestBody = new SubRequestBody();
        subRequestBody.setSubCode("SubCode0001");
        subRequestBody.setSubList(new ArrayList<>(Arrays.asList(1, 2, 3)));
        body.setSubBody(subRequestBody);
        body.setSubBody2(subRequestBody);

        Response<ResponseBody<SubResponseBody>> response = iProviderService.complexService(request, "arg1");

        System.out.println(JacksonUtils.toJsonString(response));
    }
}
