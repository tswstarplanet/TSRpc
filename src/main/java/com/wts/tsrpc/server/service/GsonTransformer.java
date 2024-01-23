package com.wts.tsrpc.server.service;

import com.wts.tsrpc.utils.ArrayUtils;
import com.wts.tsrpc.utils.GsonUtils;

public class GsonTransformer extends AbstractTransform {

    public GsonTransformer() {

    }

    @Override
    public ServiceRequest transform(String body) {
        ServiceRequest request = GsonUtils.parseObject(body, ServiceRequest.class);
        if (request.getParamValues() == null) {
            request.setParamValues(new Object[request.getParamValueStrings().length]);
        }
        for (int i = 0; i < request.getParamValueStrings().length; i++) {
            request.getParamValues()[i] = GsonUtils.parseObject(request.getParamValueStrings()[i], getManager().getService(request.getServiceId()).getArgTypes()[i]);
        }
        return request;
    }

    public ServiceRequest transform(Service service, ServiceRequest request) {
        ParameterType[] parameterTypes = service.getParameterTypes();
        if (ArrayUtils.isEmpty(parameterTypes)) {
            return request;
        }
        String[] paramValueStrings = request.getParamValueStrings();
        Object[] paramValues = request.getParamValues();
        for (int i = 0; i < paramValueStrings.length; i++) {
            if (paramValueStrings[i] == null) {
                paramValues[i] = null;
                continue;
            }
            String paramValueString = paramValueStrings[i];
            ParameterType parameterType = parameterTypes[i];
            paramValues[i] = GsonUtils.parseObject(paramValueString, parameterType.getRawType());
            transform(paramValues[i], parameterType);
        }
        return null;
    }

    public void transform(Object paramValue, ParameterType parameterType) {
        if (!parameterType.isIfHaveGeneric()) {
            return;
        }
        if (parameterType.isIfCollection()) {

        } else if (parameterType.isIfMap()) {

        } else {

        }
    }
}
