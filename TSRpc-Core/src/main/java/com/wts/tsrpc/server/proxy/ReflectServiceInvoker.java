package com.wts.tsrpc.server.proxy;

import com.wts.tsrpc.common.ServiceRequest;
import com.wts.tsrpc.common.ServiceResponse;
import com.wts.tsrpc.common.ServiceResponseCode;
import com.wts.tsrpc.server.service.Service;
import com.wts.tsrpc.exception.BizException;
import com.wts.tsrpc.server.service.ServiceMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

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
            ServiceMethod serviceMethod = new ServiceMethod(request.getMethodName());
            serviceMethod.setMethodName(request.getMethodName());
            Class<?>[] argTypes = new Class[request.getArgTypeNames().length];
            for (int i = 0; i < request.getArgTypeNames().length; i++) {
                argTypes[i] = Class.forName(request.getArgTypeNames()[i]);
            }
            serviceMethod.setArgTypes(argTypes);

            Method method = chooseMethod(serviceMethod, service.getMethods(), clazz);
            Object returnValue = method.invoke(object, request.getParamValues());
            response.setCode(ServiceResponseCode.SUCCESS.getCode());
            response.setMsg(ServiceResponseCode.SUCCESS.getMsg());
            response.setBody(returnValue);
        } catch (ClassNotFoundException e) {
            throw new BizException("Clazz of class full name [" + service.getClassFullName() + "] not exist !");
        } catch (InvocationTargetException e) {
            throw new BizException("Invoke target method face exception: " + e.getMessage());
        } catch (IllegalAccessException e) {
            throw new BizException("Illegal Access Exception: " + e.getMessage());
        }

    }

    private Method chooseMethod(ServiceMethod serviceMethod, List<ServiceMethod> serviceMethods, Class<?> clazz) {
        ServiceMethod chosenMethod = serviceMethods.stream().filter(method -> method.equals(serviceMethod)).findFirst().get();
        try {
            return clazz.getMethod(serviceMethod.getMethodName(), serviceMethod.getArgTypes());
        } catch (NoSuchMethodException e) {
            throw new BizException(STR."No method of name[\{chosenMethod.getMethodName()}], arg types: [\{serviceMethod.getArgTypes()}]");
        }
    }
}
