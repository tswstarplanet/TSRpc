package com.wts.tsrpc.test;

import com.wts.tsrpc.server.service.JsonTransformer;
import com.wts.tsrpc.server.service.ParameterType;
import com.wts.tsrpc.server.service.Service;
import com.wts.tsrpc.server.service.ServiceRequest;
import com.wts.tsrpc.utils.GsonUtils;
import com.wts.tsrpc.utils.ReflectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RequestBody<S, X> {
    private String code;

    private String msg;

    private List<List<String>> list;

    private S subBody;

    private X subBody2;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<List<String>> getList() {
        return list;
    }

    public void setList(List<List<String>> list) {
        this.list = list;
    }

    public S getSubBody() {
        return subBody;
    }

    public void setSubBody(S subBody) {
        this.subBody = subBody;
    }

    public X getSubBody2() {
        return subBody2;
    }

    public void setSubBody2(X subBody2) {
        this.subBody2 = subBody2;
    }

    public void func(List<List<RequestBody<SubRequestBody, SubRequestBody>>> list, String arg1) {

    }

    public static void main(String[] args) throws NoSuchFieldException, NoSuchMethodException {
//        Field field = RequestBody.class.getDeclaredField("list");
//        Type type = field.getGenericType();
//        System.out.println();


        Method method = RequestBody.class.getMethod("func", List.class, String.class);
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        List<ParameterType> parameterTypes = new ArrayList<>();
        Class<?> clazz = (Class<?>) ((ParameterizedType) genericParameterTypes[0]).getRawType();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (int i = 0; i < genericParameterTypes.length; i++) {
            ParameterType parameterType = new ParameterType();
            ReflectUtils.settleParameterType(parameterType, genericParameterTypes[i]);
            parameterTypes.add(parameterType);
        }

        SubRequestBody subRequestBody = new SubRequestBody();
        subRequestBody.setSubCode("001");
        subRequestBody.setSubList(new ArrayList<>(Arrays.asList(1, 2)));
        RequestBody<SubRequestBody, SubRequestBody> requestBody = new RequestBody<>();
        requestBody.setCode("0001");
        requestBody.setSubBody(subRequestBody);
        requestBody.setSubBody2(subRequestBody);

        List<RequestBody<SubRequestBody, SubRequestBody>> list1 = new ArrayList<>(Arrays.asList(requestBody, requestBody));
        List<List<RequestBody<SubRequestBody, SubRequestBody>>> lists = new ArrayList<>(Arrays.asList(list1, list1));

//        System.out.println(GsonUtils.toJsonString(requestBody));

//        List<List<String>> lists = new ArrayList<>(Arrays.asList((new ArrayList<>(Arrays.asList("hello1", "world1"))), new ArrayList<>(Arrays.asList("hello2", "world2"))));
        System.out.println(GsonUtils.toJsonString(lists));

        Service service = new Service();
        service.classFullName("com.wts.tsrpc.test.RequestBody")
                .methodName("func")
                .argTypes(new Class[]{List.class, String.class});
        service.setParameterTypes(parameterTypes.toArray(new ParameterType[0]));
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setServiceId("");
//        serviceRequest.setParamValueStrings(new String[] {"[[{\"code\":\"0001\",\"msg\":null,\"list\":null,\"subBody\":{\"subCode\":\"001\",\"subList\":[1,2]},\"subBody2\":{\"subCode\":\"001\",\"subList\":[1,2]}},{\"code\":\"0001\",\"msg\":null,\"list\":null,\"subBody\":{\"subCode\":\"001\",\"subList\":[1,2]},\"subBody2\":{\"subCode\":\"001\",\"subList\":[1,2]}}],[{\"code\":\"0001\",\"msg\":null,\"list\":null,\"subBody\":{\"subCode\":\"001\",\"subList\":[1,2]},\"subBody2\":{\"subCode\":\"001\",\"subList\":[1,2]}},{\"code\":\"0001\",\"msg\":null,\"list\":null,\"subBody\":{\"subCode\":\"001\",\"subList\":[1,2]},\"subBody2\":{\"subCode\":\"001\",\"subList\":[1,2]}}]]", "arg1"});
//        serviceRequest.setParamValues(new Object[serviceRequest.getParamValueStrings().length]);

        JsonTransformer jsonTransformer = new JsonTransformer();

//        jsonTransformer.transform(service, serviceRequest);
    }


}
