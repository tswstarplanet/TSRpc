package com.wts.tsrpc.test;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.wts.tsrpc.server.service.ServiceRequest;
import com.wts.tsrpc.utils.GsonUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class Request<B> {
    private String reqId;

    private Integer count;

    private B body;

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public B getBody() {
        return body;
    }

    public void setBody(B body) {
        this.body = body;
    }

    public static void main(String[] args) {
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setServiceId("complexService");

        Request<RequestBody<SubRequestBody, SubRequestBody>> request = new Request<>();
        request.setReqId("REQ00001");
        request.setCount(1);

        RequestBody<SubRequestBody, SubRequestBody> body = new RequestBody<>();
        body.setCode("REQ00002");
        body.setMsg("请求00001");
//        body.setList(new ArrayList<String>(Arrays.asList("Hello", "Java")));
        request.setBody(body);

        SubRequestBody subRequestBody = new SubRequestBody();
        subRequestBody.setSubCode("SubCode0001");
        subRequestBody.setSubList(new ArrayList<>(Arrays.asList(1, 2, 3)));

        body.setSubBody(subRequestBody);
        body.setSubBody2(subRequestBody);

        var gson = new Gson();
        System.out.println(gson.toJson(request));

        serviceRequest.setParamValueStrings(new String[]{ GsonUtils.toJsonString(request), "Sun"});

        String json = GsonUtils.toJsonString(serviceRequest);

        String json1 = GsonUtils.toJsonString(request);


//        Type type = request.getClass().getGenericSuperclass();
        Type type = new TypeToken<Request2>(){}.getType();
        Request<RequestBody> request1 = gson.fromJson(json1, type);
        System.out.println();
    }

//    public static Object parse(String json, )
}
