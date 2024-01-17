package com.wts.tsrpc.manage;

import com.wts.tsrpc.exception.BizException;
import com.wts.tsrpc.service.Service;
import com.wts.tsrpc.service.ServiceRequest;
import com.wts.tsrpc.service.ServiceResponse;
import com.wts.tsrpc.service.ServiceResponseCode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectDispatcher implements Dispatcher {
    private Manager manager;

    public ReflectDispatcher() {

    }

    public Dispatcher manager(Manager manager) {
        this.manager = manager;
        return this;
    }


    public ServiceResponse dispatch(ServiceRequest request) {
        Service service = manager.getService(request.getServiceId());
        if (service == null) {
            throw new BizException("Service of serviceId[" + request.getServiceId() + "] not exist !");
        }
        try {
            Class<?> clazz = Class.forName(service.getClassFullName());
            Method method = clazz.getMethod(service.getMethodName(), service.getArgTypes());
            Object returnValue = method.invoke(manager.getObject(request.getServiceId()), request.getParamValues());
            return new ServiceResponse(ServiceResponseCode.SUCCESS.getCode(), ServiceResponseCode.SUCCESS.getMsg(), returnValue);
//            return ResponseUtils.buildCommonHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, HttpContentType.APPLICATION_JSON.getContentType(), content);
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

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }
}
