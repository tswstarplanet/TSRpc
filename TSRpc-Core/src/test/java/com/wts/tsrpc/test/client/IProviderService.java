package com.wts.tsrpc.test.client;

import com.wts.tsrpc.test.server.*;

import java.util.List;

public interface IProviderService {
    Response<ResponseBody<SubResponseBody>> complexService(Request<RequestBody<SubRequestBody, SubRequestBody>> request, String test);

    String func(List<List<String>> list);
}
