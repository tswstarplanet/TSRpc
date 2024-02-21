package com.wts.tsrpc.test.server;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;

public class SubProviderService extends ProviderService {
    @Override
    public Response complexService(Request request, String test) {
        Response<ResponseBody<SubResponseBody>> response = new Response<>();
        response.setRspCode("0000");
        response.setCount(2);

        ResponseBody<SubResponseBody> body = new ResponseBody<>();
        body.setResCode("0001");
        body.setResMsg("成功");
        body.setResList(Lists.newArrayList("hello", "world"));

        SubResponseBody subResponseBody = new SubResponseBody();
        subResponseBody.setSubResCode("SubRes0001");
        subResponseBody.setSubResList(new ArrayList<>(Arrays.asList(4, 5, 6)));
        body.setSubResBody(subResponseBody);


        response.setBody(body);
        return response;
    }
}
