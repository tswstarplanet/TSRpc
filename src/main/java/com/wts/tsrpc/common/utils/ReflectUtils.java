package com.wts.tsrpc.common.utils;

import com.wts.tsrpc.server.service.ParameterType;
import com.wts.tsrpc.server.service.ServiceMethod;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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

    public static String getMethodSignature(ServiceMethod serviceMethod) {
        var builder = new StringBuilder();
        builder.append(STR."\{serviceMethod.getMethodName()}-");
        var argTypes = serviceMethod.getArgTypes();
        for (var argType : argTypes) {
            builder.append(STR."\{argType.getName()}-");
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
}
