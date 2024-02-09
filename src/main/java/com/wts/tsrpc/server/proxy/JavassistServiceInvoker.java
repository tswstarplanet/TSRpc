package com.wts.tsrpc.server.proxy;

import com.wts.tsrpc.common.ServiceRequest;
import com.wts.tsrpc.common.ServiceResponse;
import com.wts.tsrpc.common.ServiceResponseCode;
import com.wts.tsrpc.common.proxy.ClassTool;
import com.wts.tsrpc.common.proxy.ServiceWrapper;
import com.wts.tsrpc.exception.BizException;
import com.wts.tsrpc.server.manage.Manager;
import com.wts.tsrpc.server.service.Service;

public class JavassistServiceInvoker implements ServiceInvoker {

    private ServiceWrapper wrapper;

    private Service service;

    private Manager manager;



    public JavassistServiceInvoker(Service service, Manager manager) {
        this.service = service;
        this.manager = manager;
        ClassTool classTool = new ClassTool();
        classTool.manager(manager);
        this.wrapper = classTool.getOrCreateServerProxyObject(service);
    }



    @Override
    public void invoke(ServiceRequest request, ServiceResponse response, Service service) {
        Object returnValue = wrapper.callMethod(request.getMethodName(), getArgTypes(request.getArgTypeNames()), request.getParamValues());
        response.setCode(ServiceResponseCode.SUCCESS.getCode());
        response.setMsg(ServiceResponseCode.SUCCESS.getMsg());
        response.setBody(returnValue);
    }

    private Class<?>[] getArgTypes(String[] argTypeNames) {
        Class<?>[] argTypes = new Class[argTypeNames.length];
        for (int i = 0; i < argTypeNames.length; i++) {
            try {
                argTypes[i] = Class.forName(argTypeNames[i]);
            } catch (ClassNotFoundException e) {
                throw new BizException(STR."No argument type for name[\{argTypeNames[0]}]");
            }
        }
        return argTypes;
    }
}
