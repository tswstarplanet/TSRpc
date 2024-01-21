package com.wts.tsrpc.server.service;

import com.wts.tsrpc.exception.BizException;
import com.wts.tsrpc.utils.ArrayUtils;
import com.wts.tsrpc.utils.ReflectUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class Service {
    private String classFullName;

    private String methodName;

    private Class<?>[] argTypes;

    private ParameterType[] parameterTypes;

    private Class<?> returnType;

    public Service process() {
        if (StringUtils.isEmpty(classFullName)) {
            throw new BizException("ClassFullName is empty !");
        }
        if (StringUtils.isEmpty(methodName)) {
            throw new BizException("MethodName is empty !");
        }
        if (argTypes == null) {
            argTypes = new Class[0];
        }
        try {
            Class<?> clazz = Class.forName(classFullName);
            Method method = clazz.getMethod(methodName, argTypes);
            Type[] genericParameterTypes = method.getGenericParameterTypes();
            if (ArrayUtils.isEmpty(genericParameterTypes)) {
                return this;
            }
            for (int i = 0; i < genericParameterTypes.length; i++) {
                Type type = genericParameterTypes[i];
                ParameterType parameterType = new ParameterType();
                ReflectUtils.settleParameterType(parameterType, type);
            }
        } catch (ClassNotFoundException e) {
            throw new BizException(STR."The class of name \{classFullName} can not be found, exception msg: \{e.getMessage()}");
        } catch (NoSuchMethodException e) {
            throw new BizException(STR."The method of name \{methodName} can not be found, exception msg: \{e.getMessage()}");
        }
        return null;
    }



    public Service classFullName(String classFullName) {
        this.classFullName = classFullName;
        return this;
    }

    public Service methodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public Service argTypes(Class<?>[] argTypes) {
        this.argTypes = argTypes;
        return this;
    }

    public Service returnType(Class<?> returnType) {
        this.returnType = returnType;
        return this;
    }

    public String getClassFullName() {
        return classFullName;
    }

    public void setClassFullName(String classFullName) {
        this.classFullName = classFullName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getArgTypes() {
        return argTypes;
    }

    public void setArgTypes(Class<?>[] argTypes) {
        this.argTypes = argTypes;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

    public ParameterType[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(ParameterType[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public static class ClassA {

    }

    public static class ClassB extends ClassA {

    }

    public static void main(String[] args) {
        System.out.println(ClassA.class.isAssignableFrom(ClassB.class));
    }
}
