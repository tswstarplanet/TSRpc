package com.wts.tsrpc.server.service;

import com.wts.tsrpc.common.ServiceRequest;
import com.wts.tsrpc.common.ServiceResponse;
import com.wts.tsrpc.common.ServiceResponseCode;
import com.wts.tsrpc.common.Service;
import com.wts.tsrpc.exception.BizException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectServiceInvoker implements ServiceInvoker {

    private Service service;

    private Object object;

    public ReflectServiceInvoker(Service service, Object object) {
        this.service = service;
        this.object = object;
    }

    @Override
    public void invoke(ServiceRequest request, ServiceResponse response, Service service) {

        try {
            Class<?> clazz = Class.forName(service.getClassFullName());
            Method method = clazz.getMethod(service.getMethodName(), service.getArgTypes());
            Object returnValue = method.invoke(object, request.getParamValues());
            response.setCode(ServiceResponseCode.SUCCESS.getCode());
            response.setMsg(ServiceResponseCode.SUCCESS.getMsg());
            response.setBody(returnValue);
        } catch (ClassNotFoundException e) {
            throw new BizException("Clazz of class full name [" + service.getClassFullName() + "] not exist !");
        } catch (NoSuchMethodException e) {
            throw new BizException("Method of method name [" + service.getMethodName() + "] of class [" + service.getClassFullName() + "] not exist !" );
        } catch (InvocationTargetException e) {
            throw new BizException("Invoke target method face exception: " + e.getMessage());
        } catch (IllegalAccessException e) {
            throw new BizException("Illegal Access Exception: " + e.getMessage());
        }

    }
}
