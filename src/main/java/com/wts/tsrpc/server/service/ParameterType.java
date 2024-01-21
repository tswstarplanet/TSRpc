package com.wts.tsrpc.server.service;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * 自定义参数类型
 */
public class ParameterType {
    /**
     * 是否含有泛型参数
     */
    private boolean ifHaveGeneric;

    /**
     * 是否集合参数
     */
    private boolean ifCollection;

    /**
     * 是否Map参数
     */
    private boolean ifMap;

    /**
     * 原始类型
     */
    private Class<?> rawType;

    /**
     * 参数化类型列表
     */
    private List<ParameterType> parameterTypes;

    /**
     * 原始泛型定义参数名
     */
    private String rawGenericType;

    /**
     * 泛型参数到Field的映射
     */
    private Map<Type, List<Field>> typeFieldMap;

    /**
     * Field名到泛型参数的映射
     */
    private Map<String, Type> fieldNameTypeMap;

    /**
     * 容器内容类型
     */
    private List<Type> contentTypes;

    public boolean isIfHaveGeneric() {
        return ifHaveGeneric;
    }

    public void setIfHaveGeneric(boolean ifHaveGeneric) {
        this.ifHaveGeneric = ifHaveGeneric;
    }

    public boolean isIfCollection() {
        return ifCollection;
    }

    public void setIfCollection(boolean ifCollection) {
        this.ifCollection = ifCollection;
    }

    public boolean isIfMap() {
        return ifMap;
    }

    public void setIfMap(boolean ifMap) {
        this.ifMap = ifMap;
    }

    public Class<?> getRawType() {
        return rawType;
    }

    public void setRawType(Class<?> rawType) {
        this.rawType = rawType;
    }

    public List<ParameterType> getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(List<ParameterType> parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public String getRawGenericType() {
        return rawGenericType;
    }

    public void setRawGenericType(String rawGenericType) {
        this.rawGenericType = rawGenericType;
    }

    public Map<Type, List<Field>> getTypeFieldMap() {
        return typeFieldMap;
    }

    public void setTypeFieldMap(Map<Type, List<Field>> typeFieldMap) {
        this.typeFieldMap = typeFieldMap;
    }

    public Map<String, Type> getFieldNameTypeMap() {
        return fieldNameTypeMap;
    }

    public void setFieldNameTypeMap(Map<String, Type> fieldNameTypeMap) {
        this.fieldNameTypeMap = fieldNameTypeMap;
    }

    public List<Type> getContentTypes() {
        return contentTypes;
    }

    public void setContentTypes(List<Type> contentTypes) {
        this.contentTypes = contentTypes;
    }
}
