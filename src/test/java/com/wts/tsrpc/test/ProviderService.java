package com.wts.tsrpc.test;

import com.google.common.collect.Lists;
import com.wts.tsrpc.server.service.ServiceRequest;
import com.wts.tsrpc.utils.GsonUtils;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProviderService {
    public String primitiveService(String arg1, Integer arg2) {
        return arg1 + ": " + arg2;
    }

    public <T> Response<ResponseBody<SubResponseBody>> complexService(Request2 request, String code) {
        Response<ResponseBody<SubResponseBody>> response = new Response<>();
        response.setRspCode("0000");
        response.setCount(2);

        ResponseBody<SubResponseBody> body = new ResponseBody<>();
        body.setResCode("0001");
        body.setResMsg("成功");
        body.setResList(Lists.newArrayList("hello", "world"));

        SubResponseBody subResponseBody = new SubResponseBody();
        subResponseBody.setSubResCode("SubRes0001");
        subResponseBody.setSubResList(new ArrayList<>(Arrays.asList(4, 5, 6)));
        body.setSubResBody(subResponseBody);


        response.setBody(body);
        return response;
    }

    public void func(List<List<String>> list) {

    }

    public static void main(String[] args) throws NoSuchMethodException, NoSuchFieldException {
        Request2 request2 = new Request2();
        RequestBody2 requestBody2 = new RequestBody2();
        requestBody2.setCode("0001");
        requestBody2.setMsg("msg1");
        request2.setRequestBody2(requestBody2);

        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setServiceId("complexService");
        serviceRequest.setParamValueStrings(new String[] {GsonUtils.toJsonString(request2), "code1"});

        System.out.println(GsonUtils.toJsonString(serviceRequest));
    }

    public static void test1() {
        Class<?> clazz = Request.class;
        TypeVariable<? extends Class<?>>[] typeParameters = clazz.getTypeParameters();
        Field[] fields = clazz.getDeclaredFields();
        System.out.println();
    }

    public static <T> T parse(String json, Class<T> clazz, ParameterizedType type, Object object, String fieldName) {
        if (type == null) {
            Object obj = GsonUtils.parseObject(json, clazz);
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                if (checkPrimitive(field)) {
                    continue;
                }
                String tempFieldName = field.getName();
//                String fieldJson =
            }
        } else {

        }
        return null;
    }

    public static void test2() throws NoSuchFieldException {
        Request<RequestBody> request = new Request<>();
        Field field = request.getClass().getDeclaredField("body");
        Type genericType = field.getGenericType();
        System.out.println();
    }

    public static boolean checkPrimitive(Field field) {
        Class clazz = (Class) field.getGenericType();
        return false;
    }

//    public static Object parse(String json, Class<?> rawType, )
}
