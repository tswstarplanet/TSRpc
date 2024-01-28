package com.wts.tsrpc.test.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.google.common.collect.Lists;
import com.wts.tsrpc.common.ServiceRequest;
import com.wts.tsrpc.common.utils.GsonUtils;
import com.wts.tsrpc.common.utils.JacksonUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProviderService {
    public String primitiveService(String arg1, Integer arg2) {
        return arg1 + ": " + arg2;
    }

    public Response<ResponseBody<SubResponseBody>> complexService(Request<RequestBody<SubRequestBody, SubRequestBody>> request, String test) {
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

    public static void main(String[] args) throws NoSuchMethodException, NoSuchFieldException, IOException {
        transferGeneric();
    }

    public static void transferCommonStr() throws JsonProcessingException {

        Test test = new Test("a1", 1);
        String json = JacksonUtils.toJsonString(test);


        ObjectMapper objectMapper = new ObjectMapper();
        String value = "Hello";
        String value2 = objectMapper.writeValueAsString(value);
        System.out.println(value2);
        String value3 = objectMapper.readValue(value, String.class);
        System.out.println(value3);

//        Gson gson = new Gson();
//        String value = "Hello World";
//        String value2 = gson.toJson(value);
//        System.out.println(value2);
//        String value3 = gson.fromJson(value, String.class);
//        System.out.println(value3);
    }

    public static void transferGeneric() throws NoSuchMethodException, NoSuchFieldException, IOException {
        Method method = ProviderService.class.getMethod("complexService", Request.class, String.class);
        Type[] types = method.getGenericParameterTypes();
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

        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setServiceId("complexService");
        serviceRequest.setParamValueStrings(new String[]{JacksonUtils.toJsonString(request), "hello"});


        System.out.println(JacksonUtils.toJsonString(serviceRequest));

        ObjectMapper objectMapper = new ObjectMapper();
        JavaType type1 = objectMapper.constructType(types[0]);
//        GenericTypeResolver.resolveType(type1, ProviderService.class);
        System.out.println(objectMapper.writeValueAsString(request));
        String value = objectMapper.writeValueAsString(request);
        InputStream inputStream = new ByteArrayInputStream(value.getBytes());
        ObjectReader objectReader = objectMapper.reader().forType(type1);
        Request<RequestBody<SubRequestBody, SubRequestBody>> request3 = objectReader.readValue(inputStream);
        System.out.println(GsonUtils.toJsonString(request3));


//        Test test = null;
//        int test = 1234;
        String test = " Hello world ! ";

        type1 = objectMapper.constructType(types[1]);
        String testStr = objectMapper.writeValueAsString(test);
        System.out.println(testStr);
        InputStream inputStream1 = new ByteArrayInputStream(testStr.getBytes());
        ObjectReader objectReader1 = objectMapper.reader().forType(type1);
        String test1 = objectReader1.readValue(inputStream1);
        System.out.println();


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
