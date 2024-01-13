package com.wts.tsrpc.manage;

import com.wts.tsrpc.exception.BizException;
import com.wts.tsrpc.exception.ServiceDuplicateException;
import com.wts.tsrpc.service.Service;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Manager {

    private Application application;
    private Map<String, Service> serviceMap = new ConcurrentHashMap<>();

    private Map<String, Object> serviceObjectMap = new ConcurrentHashMap<>();

    private static final Object serviceLock = new Object();

    private static final Object objectLock = new Object();

    public Manager addService(String serviceId, Service service) throws ServiceDuplicateException {
        if (StringUtils.isEmpty(serviceId)) {
            throw new BizException("ServiceId can not be empty !");
        }
        if (service == null) {
            throw new BizException("Service can not be null !");
        }
        synchronized (serviceLock) {
            if (serviceMap.containsKey(serviceId)) {
                throw new ServiceDuplicateException();
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
            if (serviceMap.containsKey(serviceId)) {
                throw new BizException("The object has been exist in the object map !");
            }
            serviceObjectMap.put(serviceId, object);
        }
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
            throw new BizException("Service id is null !");
        }
        return serviceObjectMap.get(serviceId);
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
}
