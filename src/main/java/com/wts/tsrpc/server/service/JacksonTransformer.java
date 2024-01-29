package com.wts.tsrpc.server.service;

import com.wts.tsrpc.client.ClientService;
import com.wts.tsrpc.common.ServiceRequest;
import com.wts.tsrpc.common.ServiceResponse;
import com.wts.tsrpc.common.utils.JacksonUtils;
import com.wts.tsrpc.common.utils.SerialNoUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

public class JacksonTransformer extends AbstractTransform {


    public JacksonTransformer() {

    }

    @Override
    public ServiceRequest transformRequest(String body) {
        ServiceRequest request = JacksonUtils.parseObject(body, ServiceRequest.class);
        if (request.getParamValues() == null) {
            request.setParamValues(new Object[request.getParamValueStrings().length]);
        }
        for (int i = 0; i < request.getParamValueStrings().length; i++) {
            request.getParamValues()[i] = JacksonUtils.parseObject(request.getParamValueStrings()[i], getManager().getParamTypes(request.getServiceId()).get(i));
//            request.getParamValues()[i] = GsonUtils.parseObject(request.getParamValueStrings()[i], getManager().getService(request.getServiceId()).getArgTypes()[i]);
        }
        return request;
    }

    @Override
    public ServiceResponse transformResponse(String body) {
        ServiceResponse response = JacksonUtils.parseObject(body, ServiceResponse.class);
        if (response.getReturnValueString() == null) {
            response.setReturnValue(null);
            return response;
        }
        response.setReturnValue(JacksonUtils.parseObject(response.getReturnValueString(), getManager().getServiceReturnValueType(response.getServiceId())));
        return response;
    }

    @Override
    public ServiceRequest transformRequest(ClientService clientService, Object[] arguments) {
        ServiceRequest request = new ServiceRequest();
        request.setRequestId(SerialNoUtils.getGlobalSerialNo());
        request.setServiceId(clientService.getServiceId());
        if (ArrayUtils.isEmpty(arguments)) {
            request.setParamValueStrings(new String[0]);
        } else {
            request.setParamValueStrings(new String[arguments.length]);
        }
        for (int i = 0; i < arguments.length; i++) {
            request.getParamValueStrings()[i] = JacksonUtils.toJsonString(arguments[i]);
        }
        request.setRequestTime(LocalDateTime.now());
        return request;
    }

    @Override
    public String transformRequestToString(ServiceRequest request) {
        return JacksonUtils.toJsonString(request);
    }

    @Override
    public String transformObjectToString(Object object) {
        return JacksonUtils.toJsonString(object);
    }

    @Override
    public Object transformReturnValueObject(String body, Type type) {
        return JacksonUtils.parseObject(body, type);
    }

    //    public ServiceRequest transform(Service service, ServiceRequest request) {
//        ParameterType[] parameterTypes = service.getParameterTypes();
//        if (ArrayUtils.isEmpty(parameterTypes)) {
//            return request;
//        }
//        String[] paramValueStrings = request.getParamValueStrings();
//        Object[] paramValues = request.getParamValues();
//        for (int i = 0; i < paramValueStrings.length; i++) {
//            if (paramValueStrings[i] == null) {
//                paramValues[i] = null;
//                continue;
//            }
//            String paramValueString = paramValueStrings[i];
//            ParameterType parameterType = parameterTypes[i];
//            paramValues[i] = GsonUtils.parseObject(paramValueString, parameterType.getRawType());
//            transform(paramValues[i], parameterType);
//        }
//        return null;
//    }
//
//    public void transform(Object paramValue, ParameterType parameterType) {
//        if (!parameterType.isIfHaveGeneric()) {
//            return;
//        }
//        if (parameterType.isIfCollection()) {
//
//        } else if (parameterType.isIfMap()) {
//
//        } else {
//
//        }
//    }
}
