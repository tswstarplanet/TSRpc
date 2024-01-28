package com.wts.tsrpc.client.utils;

import com.wts.tsrpc.client.ClientService;
import com.wts.tsrpc.common.ServiceRequest;
import com.wts.tsrpc.common.utils.SerialNoUtils;

public class ClientInvokeUtils {

    public static ServiceRequest buildServiceRequest(ClientService clientService, Object[] arguments) {
        ServiceRequest request = new ServiceRequest();
        request.setRequestId(SerialNoUtils.getGlobalSerialNo());
        request.setServiceId(clientService.getServiceId());

        return null;
    }

    public void func() {
        System.out.println("func");
    }



}
