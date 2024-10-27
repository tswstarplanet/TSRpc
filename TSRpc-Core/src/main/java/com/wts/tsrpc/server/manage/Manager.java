package com.wts.tsrpc.server.manage;

import com.wts.tsrpc.client.ClientInvoker;
import com.wts.tsrpc.client.filter.ClientInvokerFilter;
import com.wts.tsrpc.client.service.ClientMethod;
import com.wts.tsrpc.client.service.ClientService;
import com.wts.tsrpc.common.utils.ReflectUtils;
import com.wts.tsrpc.exception.BizException;
import com.wts.tsrpc.server.service.Service;
import com.wts.tsrpc.server.service.ServiceMethod;
import com.wts.tsrpc.common.Transformer;
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

    private String serviceInvoker;

    private ServiceDispatcher serviceDispatcher;

    private final Map<String, Service> serviceMap = new ConcurrentHashMap<>();

    private final Map<String, Transformer> transformerMap = new ConcurrentHashMap<>();


    private final List<ClientInvokerFilter> defaultClientInvokerFilters = new ArrayList<>();

    private static final Object serviceLock = new Object();


    private static final Object transformLock = new Object();

//    private final Map<String, List<Type>> serviceParamTypeMap = new ConcurrentHashMap<>();

    private final Map<String, Map<String, List<Type>>> serviceMethodParamTypeMap = new ConcurrentHashMap<>();

    /**
     * Generic type map of methods of service on client side.
     */
//    private final Map<String, Map<String, List<Type>>> clientMethodParamTypeMap = new ConcurrentHashMap<>();

    private final Map<String, Map<String, Type>> clientServiceReturnValueTypeMap = new ConcurrentHashMap<>();

    private final Map<String, Map<String, ClientService>> clientServiceMap = new ConcurrentHashMap<>();

    private final Map<String, Map<String, ClientInvoker>> clientInvokerMap = new ConcurrentHashMap<>();



    public Manager() {

    }

    public Manager(Application application, String serviceInvoker, ServiceDispatcher serviceDispatcher) {
        this.application = application;
        this.serviceInvoker = serviceInvoker;
        this.serviceDispatcher = serviceDispatcher;
    }

    public Manager application(Application application) {
        this.application = application;
        return this;
    }

    public ServiceDispatcher getDispatcher() {
        return serviceDispatcher;
    }

    public Manager serviceInvoker(String serviceInvoker) {
        this.serviceInvoker = serviceInvoker;
        return this;
    }

//    public Manager addServiceParamType(String serviceId, Service service) {
//        if (StringUtils.isEmpty(serviceId)) {
//            throw new BizException("ServiceId can not be empty !");
//        }
//        if (service == null) {
//            throw new BizException("Service can not be null !");
//        }
//        if (serviceParamTypeMap.containsKey(serviceId)) {
//            throw new BizException(STR."Param type of service \{serviceId} has been existed in the map !");
//        }
//        try {
//            Method method = Class.forName(service.getClassFullName()).getMethod(service.getMethodName(), service.getArgTypes());
//            Type[] types = method.getGenericParameterTypes();
//            serviceParamTypeMap.put(serviceId, new ArrayList<>(List.of(types)));
//            return this;
//        } catch (ClassNotFoundException e) {
//            throw new BizException(STR."Class of \{service.getClassFullName()} not found !");
//        } catch (NoSuchMethodException e) {
//            throw new BizException(STR."Method of methodName: \{service.getMethodName()} and args: \{(new ArrayList<>(Arrays.asList(service.getArgTypes()))).toString()} not found !");
//        }
//    }

    public ClientInvoker getClientInvoker(String applicationKey, String serviceId) {
        return clientInvokerMap.get(applicationKey).get(serviceId);
    }

    public Manager addClientInvoker(String applicationKey, String serviceId, ClientInvoker clientInvoker) {
        if (StringUtils.isEmpty(applicationKey)) {
            throw new BizException("Application id is empty !");
        }
        if (StringUtils.isEmpty(serviceId)) {
            throw new BizException("Service id is empty !");
        }
        Map<String, ClientInvoker> tempInvokerMap = clientInvokerMap.computeIfAbsent(applicationKey, _ -> new ConcurrentHashMap<>());
        if (tempInvokerMap.containsKey(serviceId)) {
            throw new BizException(STR."Client Invoker of serviceId [\{serviceId}] has been existed !");
        }
        tempInvokerMap.putIfAbsent(serviceId, clientInvoker);
        return this;
    }

    public Manager addClientService(String applicationId, String serviceId, ClientService clientService) {
        if (StringUtils.isEmpty(applicationId)) {
            throw new BizException("Application id is empty !");
        }
        if (StringUtils.isEmpty(serviceId)) {
            throw new BizException("Service id is empty !");
        }
        Map<String, ClientService> tempServiceMap = clientServiceMap.computeIfAbsent(applicationId, _ -> new ConcurrentHashMap<>());
        if (tempServiceMap.containsKey(serviceId)) {
            throw new BizException(STR."Client Service of serviceId [\{serviceId}] has been existed !");
        }
        try {
            for (ClientMethod clientMethod : clientService.getClientMethods()) {
                Method method;
                try {
                    method = Class.forName(clientService.getClientClassFullName())
                            .getMethod(clientMethod.getClientMethodName(), clientMethod.getArgTypes());
                } catch (NoSuchMethodException e) {
                    throw new BizException(STR."Method of methodName: \{clientMethod.getClientMethodName()} and args: \{(new ArrayList<>(Arrays.asList(clientMethod.getArgTypes()))).toString()} not found !");

                }
                clientMethod.setParamTypes(Arrays.stream(method.getGenericParameterTypes()).toList())
                        .returnGenericType(method.getGenericReturnType());
                clientMethod.returnType(method.getReturnType());
            }
            clientService.settleClientMethod();
            tempServiceMap.put(serviceId, clientService);
            return this;
        } catch (ClassNotFoundException e) {
            throw new BizException(STR."Class of \{clientService.getClientClassFullName()} not found !");
        }
    }

//    public Manager addServiceReturnValueType(String serviceId, Service service) {
//        if (StringUtils.isEmpty(serviceId)) {
//            throw new BizException("ServiceId can not be empty !");
//        }
//        if (service == null) {
//            throw new BizException("Service can not be null !");
//        }
//        if (serviceReturnValueTypeMap.containsKey(serviceId)) {
//            throw new BizException(STR."Param type of service \{serviceId} has been existed in the map !");
//        }
//        try {
//            Method method = Class.forName(service.getClassFullName()).getMethod(service.getMethodName(), service.getArgTypes());
//            Type type = method.getGenericReturnType();
//            serviceReturnValueTypeMap.put(serviceId, type);
//            return this;
//        } catch (ClassNotFoundException e) {
//            throw new BizException(STR."Class of \{service.getClassFullName()} not found !");
//        } catch (NoSuchMethodException e) {
//            throw new BizException(STR."Method of methodName: \{service.getMethodName()} and args: \{(new ArrayList<>(Arrays.asList(service.getArgTypes()))).toString()} not found !");
//        }
//    }

//    public Type getClientServiceReturnValueType(String serviceId, String methodSignature) {
//        return clientServiceReturnValueTypeMap.get(serviceId).get(methodSignature);
//    }
//
//    public List<Type> getServiceMethodParamTypes(String serviceId, String methodSignature) {
//        return List.of(serviceMethodParamTypeMap.get(serviceId).get(methodSignature).toArray(new Type[0]));
//    }
//
//    public List<Type> getClientMethodParamTypes(String serviceId, String methodSignature) {
//        return List.of(clientMethodParamTypeMap.get(serviceId).get(methodSignature).toArray(new Type[0]));
//    }

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

//    public Manager addDispatcher(String dispatcherType, Dispatcher dispatcher) {
//        if (StringUtils.isEmpty(dispatcherType)) {
//            throw new BizException("ServiceId can not be empty !");
//        }
//        if (dispatcher == null) {
//            throw new BizException("Service can not be null !");
//        }
//        synchronized (dispatcherLock) {
//            if (dispatcherMap.containsKey(dispatcherType)) {
//                throw new BizException("The object has been exist in the object map !");
//            }
//            dispatcherMap.put(dispatcherType, dispatcher);
//        }
//        return this;
//    }


    public Manager addClientInvokerFilter(ClientInvokerFilter invokerFilter) {
        defaultClientInvokerFilters.add(invokerFilter);
        return this;
    }

//    public Transformer getTransform(String transformType) {
//        if (StringUtils.isEmpty(transformType)) {
//            throw new BizException("Transform type is empty !");
//        }
//        return transformerMap.get(transformType);
//    }

//    public Dispatcher getDispatcher(String dispatcherType) {
//        if (StringUtils.isEmpty(dispatcherType)) {
//            throw new BizException("Dispatcher type is empty !");
//        }
//        return dispatcherMap.get(dispatcherType);
//    }


    public List<ClientInvokerFilter> getDefaultClientInvokerFilters() {
        return List.copyOf(defaultClientInvokerFilters);
    }
}
