package com.wts.tsrpc.server.proxy;

import com.wts.tsrpc.common.ServiceRequest;
import com.wts.tsrpc.common.ServiceResponse;
import com.wts.tsrpc.common.ServiceResponseCode;
import com.wts.tsrpc.common.proxy.ClassTool;
import com.wts.tsrpc.common.proxy.ServiceWrapper;
import com.wts.tsrpc.exception.BizException;
import com.wts.tsrpc.server.manage.ServerDispatcher;
import com.wts.tsrpc.server.service.Service;

public class JavassistServiceInvoker implements ServiceInvoker {

    private ServiceWrapper wrapper;

    public JavassistServiceInvoker(Service service, ServerDispatcher dispatcher) {
        ClassTool classTool = new ClassTool();
        classTool.serverDispatcher(dispatcher);
        this.wrapper = classTool.getOrCreateServerProxyObject(service);
    }



    @Override
    public void invoke(ServiceRequest request, ServiceResponse response) {
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
