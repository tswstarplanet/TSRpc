package com.wts.tsrpc.test;

import com.wts.tsrpc.server.service.ParameterType;
import com.wts.tsrpc.utils.ReflectUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
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

    public static void func(List<List<String>> list, String arg1) {

    }

    public static void main(String[] args) throws NoSuchFieldException, NoSuchMethodException {
//        Field field = RequestBody.class.getDeclaredField("list");
//        Type type = field.getGenericType();
//        System.out.println();
        Method method = RequestBody.class.getMethod("func", List.class, String.class);
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        List<ParameterType> parameterTypes = new ArrayList<>();
        for (int i = 0; i < genericParameterTypes.length; i++) {
            ParameterType parameterType = new ParameterType();
            ReflectUtils.settleParameterType(parameterType, genericParameterTypes[i]);
            parameterTypes.add(parameterType);
        }
    }

}
