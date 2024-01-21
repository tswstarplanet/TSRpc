package com.wts.tsrpc.utils;

public class ArrayUtils {

    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

}
