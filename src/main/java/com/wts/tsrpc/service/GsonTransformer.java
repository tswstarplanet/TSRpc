package com.wts.tsrpc.service;

import com.wts.tsrpc.utils.JsonUtils;

public class GsonTransformer implements Transformer {

    public GsonTransformer() {

    }

    @Override
    public ServiceRequest transform(String transformType) {
        return JsonUtils.parseObject(transformType, ServiceRequest.class);
    }
}
