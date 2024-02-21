package com.wts.tsrpc.test.server;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.wts.tsrpc.common.ServiceRequest;
import com.wts.tsrpc.common.utils.GsonUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Request<B> {
    private String reqId;

    private Long count;

    private B body;

    List<Test> list;

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public B getBody() {
        return body;
    }

    public void setBody(B body) {
        this.body = body;
    }

    public List<Test> getList() {
        return list;
    }

    public void setList(List<Test> list) {
        this.list = list;
    }

    public static void main(String[] args) {


        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setServiceId("complexService");

        Request<RequestBody<SubRequestBody, SubRequestBody>> request = new Request<>();
        request.setReqId("REQ00001");
        request.setCount(1L);

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

//        serviceRequest.setParamValueStrings(new String[]{ GsonUtils.toJsonString(request), "Sun"});


        MyParameterizedType requestBodyParamType = new MyParameterizedType(RequestBody.class, new Type[] {SubRequestBody.class, SubRequestBody.class});

        MyParameterizedType requestParamType = new MyParameterizedType(Request.class, new Type[]{requestBodyParamType});

        String json = GsonUtils.toJsonString(request);

        Request<RequestBody<SubRequestBody, SubRequestBody>> request1 = gson.fromJson(json, requestParamType);

//        String json = GsonUtils.toJsonString(serviceRequest);

        String json1 = GsonUtils.toJsonString(request);


//        Type type = request.getClass().getGenericSuperclass();
        Type type = new TypeToken<Request2>(){}.getType();
//        Request<RequestBody> request1 = gson.fromJson(json1, type);
        System.out.println();





    }

//    public static Object parse(String json, )
}
