package com.wts.tsrpc.server.service;

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
}
