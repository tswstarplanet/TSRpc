package com.wts.tsrpc.server.filter;

import com.wts.tsrpc.server.service.Service;
import com.wts.tsrpc.server.proxy.ServiceInvoker;
import com.wts.tsrpc.common.ServiceRequest;
import com.wts.tsrpc.common.ServiceResponse;

import java.util.ArrayList;
import java.util.List;

public class ServerInvokerFilterChain implements ServerInvokerFilter {

    private int pos = 0;

    private Service service;

    private ServiceInvoker serviceInvoker;

    private List<ServerInvokerFilter> invokerFilters = new ArrayList<>();

    public ServerInvokerFilterChain() {
    }



    public void addFilter(ServerInvokerFilter invokerFilter) {
        if (invokerFilters == null) {
            invokerFilters = new ArrayList<>();
        }
        invokerFilters.add(invokerFilter);
    }

    @Override
    public void doFilter(ServiceRequest serviceRequest, ServiceResponse serviceResponse, ServerInvokerFilterChain filterChain) {
        if (pos == invokerFilters.size()) {
            serviceInvoker.invoke(serviceRequest, serviceResponse, service);
            return;
        }
        invokerFilters.get(pos++).doFilter(serviceRequest, serviceResponse, filterChain);
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public ServiceInvoker getServiceInvoker() {
        return serviceInvoker;
    }

    public void setServiceInvoker(ServiceInvoker serviceInvoker) {
        this.serviceInvoker = serviceInvoker;
    }

    public List<ServerInvokerFilter> getInvokerFilters() {
        return invokerFilters;
    }

    public void setInvokerFilters(List<ServerInvokerFilter> invokerFilters) {
        this.invokerFilters = invokerFilters;
    }
}
