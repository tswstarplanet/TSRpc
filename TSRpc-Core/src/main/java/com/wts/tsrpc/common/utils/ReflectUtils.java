package com.wts.tsrpc.common.utils;

import com.wts.tsrpc.server.service.ParameterType;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class ReflectUtils {
    public static boolean isCollection(Class<?> clazz) {
        return clazz.isArray() || Collection.class.isAssignableFrom(clazz);
    }

    public static boolean isMap(Class<?> clazz) {
        return clazz.isAssignableFrom(Map.class);
    }

    public static void settleParameterType(@NotNull ParameterType parameterType, Type type) {
        parameterType.setRawType((Class<?>) (type instanceof ParameterizedType ? ((ParameterizedType) type).getRawType() : type));
        parameterType.setIfHaveGeneric(type instanceof ParameterizedType);
        parameterType.setIfCollection(ReflectUtils.isCollection(type instanceof Class<?> ? (Class<?>) type : ((Class<?>) ((ParameterizedType) type).getRawType())));
        parameterType.setIfMap(ReflectUtils.isMap(type instanceof Class<?> ? (Class<?>) type : ((Class<?>) ((ParameterizedType) type).getRawType())));
        if (!parameterType.isIfHaveGeneric()) {
            return;
        }
        if (parameterType.isIfCollection()) {
            List<ParameterType> types = new ArrayList<>();
            ParameterType parameterType1 = new ParameterType();
            settleParameterType(parameterType1, ((ParameterizedType) type).getActualTypeArguments()[0]);
            types.add(parameterType1);
            parameterType.setParameterTypes(types);
        } else if (parameterType.isIfMap()) {

        } else if (parameterType.isIfHaveGeneric()) {

        }
    }

    public static String getMethodSignature(String methodName, Class<?>[] argTypes) {
        return getMethodSignature(methodName, Arrays.stream(argTypes).map(Class::getName).toList());
    }

    public static String getMethodSignature(String methodName, List<String> argTypeNames) {
        var builder = new StringBuilder();
        builder.append(STR."\{methodName}-");
        for (var argTypeName : argTypeNames) {
            builder.append(STR."\{argTypeName}-");
        }
        return builder.toString();
    }

    public static Class<?>[] getClazzFromName(String[] argTypeNames) {
        Class<?>[] classes = new Class<?>[argTypeNames.length];
        for (int i = 0; i < argTypeNames.length; i++) {
            try {
                classes[i] = Class.forName(argTypeNames[i]);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return classes;
    }

    public static String getArgsString(Class<?>[] classes) {
//        if (ArrayUtils.isEmpty(classes)) {
//            return "";
//        }
//        StringBuilder builder = new StringBuilder("new java.lang.Class<?>[]{");
//        for (int i = 0; i < classes.length; i++) {
//            builder.append(STR."java.lang.Class.forName(\"\{classes[i].getName()}\")");
//            if (i < classes.length - 1) {
//                builder.append(", ");
//            }
//        }
//        builder.append("}");
//        return builder.toString();
        if (ArrayUtils.isEmpty(classes)) {
            return "";
        }
        StringBuilder builder = new StringBuilder("new java.lang.String[]{");
        for (int i = 0; i < classes.length; i++) {
            builder.append(STR."\"\{classes[i].getName()}\"");
            if (i < classes.length - 1) {
                builder.append(", ");
            }
        }
        builder.append("}");
        return builder.toString();
    }


}
