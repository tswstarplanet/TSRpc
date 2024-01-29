package com.wts.tsrpc.test.client;

import com.wts.tsrpc.client.ClientInvoker;
import com.wts.tsrpc.client.ClientService;
import com.wts.tsrpc.client.RandomLoadBalancer;
import com.wts.tsrpc.common.utils.JacksonUtils;
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
        clientService.setServiceId("complexService");
        clientService.setApplicationId("ServerApplication1");
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

        Response<ResponseBody<SubResponseBody>> response = (Response<ResponseBody<SubResponseBody>>) clientInvoker.invoke(new Object[]{request, "arg1"});

        System.out.println(JacksonUtils.toJsonString(response));
//        Method method = IProviderService.class.getMethod("complexService", Request.class, String.class);
//        Type[] types = method.getGenericParameterTypes();
//        Type returnType = method.getGenericReturnType();
//        clientService.set
    }
}
