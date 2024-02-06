package com.wts.tsrpc.common.proxy;

public interface ServiceWrapper {

    Object callMethod(String methodName, Class<?>[] argTypes, Object[] arguments);

}
