package com.wts.tsrpc.test.client;

import com.wts.tsrpc.test.server.*;

public interface IProviderService {
    Response<ResponseBody<SubResponseBody>> complexService(Request<RequestBody<SubRequestBody, SubRequestBody>> request, String test);
}
