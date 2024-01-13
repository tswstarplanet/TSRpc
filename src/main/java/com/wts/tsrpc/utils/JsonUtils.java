package com.wts.tsrpc.utils;

import com.google.gson.Gson;

public class JsonUtils {
    private static final Gson gson = new Gson();

    public static String toJsonString(Object o) {
        return gson.toJson(o);
    }


}
