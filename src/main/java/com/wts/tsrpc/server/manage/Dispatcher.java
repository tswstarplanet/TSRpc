package com.wts.tsrpc.server.manage;

import com.wts.tsrpc.common.ServiceRequest;
import com.wts.tsrpc.common.ServiceResponse;
import com.wts.tsrpc.common.Service;
import com.wts.tsrpc.exception.BizException;
import com.wts.tsrpc.server.filter.InvokerFilter;
import com.wts.tsrpc.server.filter.InvokerFilterChain;
import com.wts.tsrpc.server.service.*;

import java.util.List;

public class Dispatcher {
    private Manager manager;

    public Dispatcher() {

    }

    public Dispatcher manager(Manager manager) {
        this.manager = manager;
        return this;
    }


    public ServiceResponse dispatch(ServiceRequest request) {
        List<InvokerFilter> invokerFilters = manager.getDefaultInvokerFilters();

        Service service = manager.getService(request.getServiceId());
        if (service == null) {
            throw new BizException("Service of serviceId[" + request.getServiceId() + "] not exist !");
        }
        InvokerFilterChain filterChain = new InvokerFilterChain();
        filterChain.setService(service);
        filterChain.setInvokerFilters(invokerFilters);
        ServiceInvoker invoker;

        switch (manager.getServiceInvoker()) {
            case "reflect" -> invoker = new ReflectServiceInvoker(service, manager.getObject(request.getServiceId()));
            default -> invoker = new ReflectServiceInvoker(service, manager.getObject(request.getServiceId()));
        }
        filterChain.setServiceInvoker(invoker);

        ServiceResponse response = new ServiceResponse();
        filterChain.doFilter(request, response, filterChain);
        return response;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }
}
