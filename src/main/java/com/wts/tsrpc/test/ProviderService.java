package com.wts.tsrpc.test;

import com.google.common.collect.Lists;
import com.wts.tsrpc.utils.GsonUtils;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProviderService {
    public String primitiveService(String arg1, Integer arg2) {
        return arg1 + ": " + arg2;
    }

    public <T> Response<ResponseBody<SubResponseBody>> complexService(Request<RequestBody<SubRequestBody, SubRequestBody>> request, String code) {
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
        test2();
//        parse("{\"reqId\":\"REQ00001\",\"count\":1,\"body\":{\"code\":\"CODE00001\",\"list\":[\"Hello\",\"Java\"]}}", Request.class, null, null, null);
        test1();
        Method method = ProviderService.class.getMethod("complexService", Request.class, String.class);
        Parameter[] typeParameters = method.getParameters();
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        Type[] actualTypeArguments = ((ParameterizedType) genericParameterTypes[0]).getActualTypeArguments();
//        typeParameters[0].get
        System.out.println();
//        method = ProviderService.class.getMethod("func", List.class);
//        genericParameterTypes = method.getGenericParameterTypes();
        System.out.println();

        Type genericReturnType = method.getGenericReturnType();
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
