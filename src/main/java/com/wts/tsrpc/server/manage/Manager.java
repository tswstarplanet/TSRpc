package com.wts.tsrpc.server.manage;

import com.wts.tsrpc.exception.BizException;
import com.wts.tsrpc.server.filter.InvokerFilter;
import com.wts.tsrpc.server.service.Service;
import com.wts.tsrpc.server.service.Transformer;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Manager {

    private Application application;
    private Map<String, Service> serviceMap = new ConcurrentHashMap<>();

    private final Map<String, Object> serviceObjectMap = new ConcurrentHashMap<>();

    private final Map<String, Transformer> transformerMap = new ConcurrentHashMap<>();

    private final Map<String, Dispatcher> dispatcherMap = new ConcurrentHashMap<>();

    private List<InvokerFilter> defaultInvokerFilters = new ArrayList<>();

    private static final Object serviceLock = new Object();

    private static final Object objectLock = new Object();

    private static final Object transformLock = new Object();

    private static final Object dispatcherLock = new Object();

    private static final Manager manager = new Manager();

    private static final Map<String, List<Type>> serviceParamTypeMap = new ConcurrentHashMap<>();

    private String serviceInvoker;

    private Dispatcher dispatcher;

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    public static Manager getManager() {
        return manager;
    }

    public Manager() {

    }

    public Manager application(Application application) {
        this.application = application;
        return this;
    }

    public Manager serviceInvoker(String serviceInvoker) {
        this.serviceInvoker = serviceInvoker;
        return this;
    }

    public Manager addServiceParamType(String serviceId, Service service) {
        if (StringUtils.isEmpty(serviceId)) {
            throw new BizException("ServiceId can not be empty !");
        }
        if (service == null) {
            throw new BizException("Service can not be null !");
        }
        if (serviceParamTypeMap.containsKey(serviceId)) {
            throw new BizException(STR."Param type of service \{serviceId} has been existed in the map !");
        }
        try {
            Method method = Class.forName(service.getClassFullName()).getMethod(service.getMethodName(), service.getArgTypes());
            Type[] types = method.getGenericParameterTypes();
            serviceParamTypeMap.put(serviceId, new ArrayList<>(List.of(types)));
            return this;
        } catch (ClassNotFoundException e) {
            throw new BizException(STR."Class of \{service.getClassFullName()} not found !");
        } catch (NoSuchMethodException e) {
            throw new BizException(STR."Method of methodName: \{service.getMethodName()} and args: \{(new ArrayList<>(Arrays.asList(service.getArgTypes()))).toString()} not found !");
        }
    }

    public List<Type> getParamTypes(String serviceId) {
        return List.of(serviceParamTypeMap.get(serviceId).toArray(new Type[0]));
    }

    public Manager addService(String serviceId, Service service) {
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
            serviceMap.put(serviceId, service);
        }
        return this;
    }

    public Manager addServiceObj(String serviceId, Object object) {
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

    public Manager addTransformer(String transformType, Transformer transformer) {
        if (StringUtils.isEmpty(transformType)) {
            throw new BizException("ServiceId can not be empty !");
        }
        if (transformer == null) {
            throw new BizException("Service can not be null !");
        }
        synchronized (transformLock) {
            if (transformerMap.containsKey(transformType)) {
                throw new BizException("The object has been exist in the object map !");
            }
            transformerMap.put(transformType, transformer);
        }
        return this;
    }

    public Manager addDispatcher(String dispatcherType, Dispatcher dispatcher) {
        if (StringUtils.isEmpty(dispatcherType)) {
            throw new BizException("ServiceId can not be empty !");
        }
        if (dispatcher == null) {
            throw new BizException("Service can not be null !");
        }
        synchronized (dispatcherLock) {
            if (dispatcherMap.containsKey(dispatcherType)) {
                throw new BizException("The object has been exist in the object map !");
            }
            dispatcherMap.put(dispatcherType, dispatcher);
        }
        return this;
    }

    public Manager dispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
        return this;
    }

    public Manager addInvokerFilter(InvokerFilter invokerFilter) {
        defaultInvokerFilters.add(invokerFilter);
        return this;
    }

    public Service getService(String serviceId) {
        if (StringUtils.isEmpty(serviceId)) {
            throw new BizException("Service id is null !");
        }
        return serviceMap.get(serviceId);
    }

    public Object getObject(String serviceId) {
        if (StringUtils.isEmpty(serviceId)) {
            throw new BizException("Service id is empty !");
        }
        return serviceObjectMap.get(serviceId);
    }

    public Transformer getTransform(String transformType) {
        if (StringUtils.isEmpty(transformType)) {
            throw new BizException("Transform type is empty !");
        }
        return transformerMap.get(transformType);
    }

    public Dispatcher getDispatcher(String dispatcherType) {
        if (StringUtils.isEmpty(dispatcherType)) {
            throw new BizException("Dispatcher type is empty !");
        }
        return dispatcherMap.get(dispatcherType);
    }

    public List<InvokerFilter> getDefaultInvokerFilters() {
        return List.copyOf(defaultInvokerFilters);
    }

    public Map<String, Service> getServiceMap() {
        return serviceMap;
    }

    public void setServiceMap(Map<String, Service> serviceMap) {
        this.serviceMap = serviceMap;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public String getServiceInvoker() {
        return serviceInvoker;
    }
}
