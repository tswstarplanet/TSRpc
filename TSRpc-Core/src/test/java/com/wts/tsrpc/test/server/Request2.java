package com.wts.tsrpc.test.server;

import com.wts.tsrpc.common.ServiceRequest;
import com.wts.tsrpc.common.utils.GsonUtils;

public class Request2 {
    private String code;

    private RequestBody2 requestBody2;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public RequestBody2 getRequestBody2() {
        return requestBody2;
    }

    public void setRequestBody2(RequestBody2 requestBody2) {
        this.requestBody2 = requestBody2;
    }

    public static void main(String[] args) {
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setServiceId("complexService");

        Request2 request2 = new Request2();
        request2.setCode("00001");

        RequestBody2 requestBody2 = new RequestBody2();
        requestBody2.setCode("00002");
        requestBody2.setMsg("msg00002");
        request2.setRequestBody2(requestBody2);

//        serviceRequest.setParamValueStrings(new String[]{GsonUtils.toJsonString(request2), "arg1"});
        System.out.println(GsonUtils.toJsonString(serviceRequest));
    }
}
