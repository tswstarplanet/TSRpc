package com.wts.tsrpc.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class GsonUtils {
    private static final Gson gson = new Gson();

    public static String toJsonString(Object o) {
        return gson.toJson(o);
    }

    public static  <T> T parseObject(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public static JsonArray toJsonArray(String json) {
        return JsonParser.parseString(json).getAsJsonArray();
    }
}
