package com.wts.tsrpc.server.manage;

import com.wts.tsrpc.common.ServiceRequest;
import com.wts.tsrpc.common.ServiceResponse;
import com.wts.tsrpc.common.utils.ReflectUtils;
import com.wts.tsrpc.server.proxy.JavassistServiceInvoker;
import com.wts.tsrpc.server.service.Service;
import com.wts.tsrpc.exception.BizException;
import com.wts.tsrpc.server.filter.ServerInvokerFilter;
import com.wts.tsrpc.server.filter.ServerInvokerFilterChain;
import com.wts.tsrpc.server.proxy.ReflectServiceInvoker;
import com.wts.tsrpc.server.proxy.ServiceInvoker;
import com.wts.tsrpc.server.service.ServiceMethod;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerDispatcher {

    private final List<ServerInvokerFilter> defaultServiceInvokerFilters = new ArrayList<>();

    private final Map<String, Object> serviceObjectMap = new ConcurrentHashMap<>();

    private final Map<String, Service> serviceMap = new ConcurrentHashMap<>();

    private final Map<String, Map<String, List<Type>>> serviceMethodParamTypeMap = new ConcurrentHashMap<>();

    private static final Object serviceLock = new Object();

    private String serviceInvoker;

    private static final Object objectLock = new Object();

    public ServerDispatcher() {

    }

    public ServerDispatcher(String serviceInvoker) {
        this.serviceInvoker = serviceInvoker;
    }

    public ServiceResponse dispatch(ServiceRequest request) {

        Service service = getService(request.getServiceId());
        if (service == null) {
            throw new BizException("Service of serviceId[" + request.getServiceId() + "] not exist !");
        }
        ServerInvokerFilterChain filterChain = new ServerInvokerFilterChain();
        filterChain.setService(service);
        filterChain.setInvokerFilters(defaultServiceInvokerFilters);
        ServiceInvoker invoker;

        switch (getServiceInvoker()) {
            case "reflect" -> invoker = new ReflectServiceInvoker(service, getServiceObject(request.getServiceId()));
            case "javassist" -> invoker = new JavassistServiceInvoker(service, this);
            default -> invoker = new ReflectServiceInvoker(service, getServiceObject(request.getServiceId()));
        }
        filterChain.setServiceInvoker(invoker);

        ServiceResponse response = new ServiceResponse();
        response.setRequestId(request.getRequestId());
        filterChain.doFilter(request, response, filterChain);
        return response;
    }

    public ServerDispatcher addServiceInvokerFilter(ServerInvokerFilter invokerFilter) {
        defaultServiceInvokerFilters.add(invokerFilter);
        return this;
    }

    public List<ServerInvokerFilter> getDefaultServiceInvokerFilters() {
        return List.copyOf(defaultServiceInvokerFilters);
    }

    public ServerDispatcher addService(String serviceId, Service service) {
        if (StringUtils.isEmpty(serviceId)) {
            throw new BizException("ServiceId can not be empty !");
        }
        if (service == null) {
            throw new BizException("Service can not be null !");
        }
        synchronized (serviceLock) {
            if (serviceMap.containsKey(serviceId)) {
                throw new BizException("The service has been exist in the service map !");
            }
            try {
                Class<?> clazz = Class.forName(service.getClassFullName());
                serviceMethodParamTypeMap.putIfAbsent(serviceId, new ConcurrentHashMap<>());
                for (ServiceMethod serviceMethod : service.getMethods()) {
                    Method method = clazz.getMethod(serviceMethod.getMethodName(), serviceMethod.getArgTypes());
                    serviceMethod.setParameterTypes(method.getGenericParameterTypes());
                    serviceMethod.setReturnType(method.getReturnType());

                    serviceMethodParamTypeMap.get(serviceId).put(ReflectUtils.getMethodSignature(serviceMethod.getMethodName(), serviceMethod.getArgTypes()), new ArrayList<>(Arrays.asList(serviceMethod.getParameterTypes())));
                }
                serviceMap.put(serviceId, service);

            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return this;
    }

    public ServerDispatcher addServiceObj(String serviceId, Object object) {
        if (StringUtils.isEmpty(serviceId)) {
            throw new BizException("ServiceId can not be empty !");
        }
        if (object == null) {
            throw new BizException("Service can not be null !");
        }
        synchronized (objectLock) {
            if (serviceObjectMap.containsKey(serviceId)) {
                throw new BizException("The object has been exist in the object map !");
            }
            serviceObjectMap.put(serviceId, object);
        }
        return this;
    }

    public Object getServiceObject(String serviceId) {
        if (StringUtils.isEmpty(serviceId)) {
            throw new BizException("Service id is empty !");
        }
        return serviceObjectMap.get(serviceId);
    }

    public Service getService(String serviceId) {
        if (StringUtils.isEmpty(serviceId)) {
            throw new BizException("Service id is null !");
        }
        return serviceMap.get(serviceId);
    }

    public String getServiceInvoker() {
        return serviceInvoker;
    }

    public List<Type> getServiceMethodParamTypes(String serviceId, String methodSignature) {
        return List.of(serviceMethodParamTypeMap.get(serviceId).get(methodSignature).toArray(new Type[0]));
    }
}
