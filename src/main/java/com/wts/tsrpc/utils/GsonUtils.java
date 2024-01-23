package com.wts.tsrpc.utils;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class GsonUtils {
    private static final Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();

    public static String toJsonString(Object o) {
        return gson.toJson(o);
    }

    public static  <T> T parseObject(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public static JsonArray toJsonArray(String json) {
        return JsonParser.parseString(json).getAsJsonArray();
    }

    private static class StringTypeAdapter extends TypeAdapter<String> {
        @Override
        public void write(JsonWriter out, String value) throws IOException {
            if (value == null) {

            }
        }

        @Override
        public String read(JsonReader in) throws IOException {
            return null;
        }
    }

    public static void main(String[] args) {
//        System.out.println(toJsonString(""));
        System.out.println(parseObject(" ", String.class));
    }
}
