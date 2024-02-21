package com.wts.tsrpc.common.utils;

import java.util.UUID;

public class SerialNoUtils {

    public static String getGlobalSerialNo() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
