package com.wts.tsrpc.test.client;

import com.wts.tsrpc.test.server.ProviderService;

public class Main {
    public static void main(String[] args) throws NoSuchMethodException {
//        Manager manager = new Manager();
////        HttpClient client = (new HttpClient("localhost", 8866, 10))
////                .manager(manager);
//        ClientService clientService = new ClientService();
//        Application application = (new Application())
//                .applicationId("ServiceProvider")
//                .version("1.0");
//        clientService.setServiceId("complexService");
//        clientService.setClientClassFullName("com.wts.tsrpc.test.client.IProviderService");
//        clientService.setServiceApplicationId(application.getApplicationId());
//        clientService.setServiceApplicationVersion(application.getVersion());
//
//        clientService.setClientMethods(new ArrayList<>(Arrays.asList(
//                (new ClientMethod()).clientMethodName("complexService")
//                        .argTypes(new Class<?>[]{ Request.class, String.class} )
//                        , (new ClientMethod().clientMethodName("func").argTypes(new Class<?>[]{ List.class })))));
//
////        clientService.setArgTypes(new Class[]{Request.class, String.class});
////        clientService.setClientClassFullName("com.wts.tsrpc.test.client.IProviderService");
////        clientService.setClientMethodName("complexService");
//        clientService.setTransformType("jackson");
//        manager.addClientService("ServerApplication1", "providerService", clientService)
//                .addTransformer("jackson", new JacksonTransformer());
//        ClientInvoker clientInvoker = new ClientInvoker();
//        Registry registry = new NacosRegistry("127.0.0.1:8848", "test_namespace");
//
//        registry.subscribe(application);
//
//        LoadBalancer loadBalancer = new RandomLoadBalancer(registry);
//        clientInvoker.clientService(clientService)
//                .manager(manager)
//                .loadBalancer(loadBalancer);
//        manager.addClientInvoker(application.getKey(), clientService.getServiceId(), clientInvoker);
//        manager.addClientInvokerFilter(new LogClientInvokerFilter())
//                .addClientInvokerFilter(new CheckClientInvokerFilter());
//
//        ClassTool classTool = new ClassTool();
////        classTool.manager(manager);
//        IProviderService iProviderService = (IProviderService) classTool
//                .getOrCreateClientServiceProxy(IProviderService.class, IProviderService.class.getDeclaredMethods(), application, clientService);
//
//        Request<RequestBody<SubRequestBody, SubRequestBody>> request = new Request<>();
//        request.setList(new ArrayList<>(Arrays.asList(new Test("a", 1), new Test("b", 2))));
//        request.setReqId("REQ00001");
//        request.setCount(1L);
//        RequestBody<SubRequestBody, SubRequestBody> body = new RequestBody<>();
//        body.setCode("REQ00002");
//        body.setMsg("请求00001");
//        request.setBody(body);
//        SubRequestBody subRequestBody = new SubRequestBody();
//        subRequestBody.setSubCode("SubCode0001");
//        subRequestBody.setSubList(new ArrayList<>(Arrays.asList(1, 2, 3)));
//        body.setSubBody(subRequestBody);
//        body.setSubBody2(subRequestBody);
//
//        Response<ResponseBody<SubResponseBody>> response = iProviderService.complexService(request, "arg1");
//
//        System.out.println("Rpc result: " + JacksonUtils.toJsonString(response));
    }


    public Object callMethod(String methodName, Class<?>[] argTypes, Object[] arguments) {
        try {
            com.wts.tsrpc.test.server.ProviderService target = new ProviderService();
            if (methodName.equals("complexService") && argTypes.length == 2 && argTypes[0].getName().equals("com.wts.tsrpc.test.server.Request") && argTypes[1].getName().equals("java.lang.String")) {
                return target.complexService((com.wts.tsrpc.test.server.Request) arguments[0], (java.lang.String) arguments[1]);
            }
            if (methodName.equals("func") && argTypes.length == 1 && argTypes[0].getName().equals("java.util.List")) {
                target.func((java.util.List) arguments[0]);
                return null;
            }
            throw new RuntimeException();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

//    {
//        java.lang.Object[] args = new java.lang.Object[]{$1, $2};
//        com.wts.tsrpc.client.service.ClientMethod clientMethod = new com.wts.tsrpc.client.service.ClientMethod();
//        clientMethod.clientMethodName("complexService");
//        try {
//            clientMethod.argTypes(new java.lang.String[]{"com.wts.tsrpc.test.server.Request", "java.lang.String"});
//        } catch (Exception e) {
//            throw new com.wts.tsrpc.exception.BizException("Class not found excpetion");
//        }
//        return (com.wts.tsrpc.test.server.Response) $0.clientInvoker.invoke(args, clientMethod);
//    }

//    {
//        java.lang.Object[] args = new java.lang.Object[]{$1};
//        com.wts.tsrpc.client.service.ClientMethod clientMethod = new com.wts.tsrpc.client.service.ClientMethod();
//        clientMethod.clientMethodName("func");
//        try {
//            clientMethod.argTypes(new java.lang.String[]{"java.util.List"});
//        } catch (Exception e) {
//            throw new com.wts.tsrpc.exception.BizException("Class not found excpetion");
//        }
//        return (java.lang.String) $0.clientInvoker.invoke(args, clientMethod);
//    }

}
