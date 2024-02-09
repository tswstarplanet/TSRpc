package com.wts.tsrpc.common.proxy;

import com.wts.tsrpc.client.ClientInvoker;
import com.wts.tsrpc.client.service.ClientMethod;
import com.wts.tsrpc.client.service.ClientService;
import com.wts.tsrpc.common.utils.ReflectUtils;
import com.wts.tsrpc.exception.BizException;
import com.wts.tsrpc.server.manage.Application;
import com.wts.tsrpc.server.manage.Manager;
import com.wts.tsrpc.server.service.Service;
import com.wts.tsrpc.server.service.ServiceMethod;
import javassist.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ClassTool {
    private static final Logger logger = LoggerFactory.getLogger(ClassTool.class);

    private static final ClassPool classPool = ClassPool.getDefault();

    private static Map<String, AtomicInteger> clientProxyNameNoMap = new ConcurrentHashMap<>();

    private static Map<String, AtomicInteger> serverProxyNameToMap = new ConcurrentHashMap<>();

    private static Map<String, ServiceWrapper> proxyObjectMap = new ConcurrentHashMap<>();

    private static final Object proxyObjectLock = new Object();

    private Manager manager;

    public ClassTool manager(Manager manager) {
        this.manager = manager;
        return this;
    }

    public ServiceWrapper getOrCreateServerProxyObject(Service service) {
        if (proxyObjectMap.get(service.getServiceId()) == null) {
            synchronized (proxyObjectLock) {
                if (proxyObjectMap.get(service.getServiceId()) == null) {
                    serverProxyNameToMap.putIfAbsent(service.getServiceId(), new AtomicInteger());
                    try {
                        Class<?> serviceClazz = Class.forName(service.getClassFullName());
                        CtClass cc = classPool.makeClass(STR."\{serviceClazz.getPackageName()}.\{serviceClazz.getSimpleName()}Subclass\{serverProxyNameToMap.get(service.getServiceId()).getAndIncrement()}");
                        cc.setSuperclass(classPool.getCtClass(serviceClazz.getName()));
                        cc.setInterfaces(new CtClass[]{ classPool.get(ServiceWrapper.class.getName()) });

                        CtField serviceField = new CtField(classPool.get(serviceClazz.getName()), "service", cc);
                        serviceField.setModifiers(Modifier.PRIVATE);
                        cc.addField(serviceField);

                        CtConstructor cons = new CtConstructor(new CtClass[]{classPool.get(serviceClazz.getName())}, cc);
                        cons.setBody("{ $0.service = $1; }");
                        cc.addConstructor(cons);

                        StringBuilder callMethodBody = new StringBuilder("""
                                public Object callMethod(java.lang.String methodName, java.lang.Class[] argTypes, java.lang.Object[] arguments) {
                                    try {
                                """ + STR."\{serviceClazz.getName()} target = (\{serviceClazz.getName()}) service;");

                        for (ServiceMethod serviceMethod : service.getMethods()) {
                            callMethodBody.append(STR."if (methodName.equals(\"\{serviceMethod.getMethodName()}\") && argTypes.length == \{serviceMethod.getArgTypes().length} ");
                            for (int i = 0; i < serviceMethod.getArgTypes().length; i++) {
                                callMethodBody.append(STR." && argTypes[\{i}].getName().equals(\"\{serviceMethod.getArgTypes()[i].getName()}\")");
                            }
                            callMethodBody.append(") { ");
                            if (serviceMethod.getReturnType().equals(Void.class) || serviceMethod.getReturnType().equals(void.class)) {
                                callMethodBody.append(STR."target.\{serviceMethod.getMethodName()}(\{getArgString("arguments", serviceMethod.getArgTypes().length, serviceMethod.getArgTypes())}); return null; }");
                            } else {
                                callMethodBody.append(STR."return target.\{serviceMethod.getMethodName()}(\{getArgString("arguments", serviceMethod.getArgTypes().length, serviceMethod.getArgTypes())}); }");
                            }


//                            CtClass[] ctClasses = new CtClass[serviceMethod.getArgTypes().length];
//                            setMethodArgTypes(ctClasses, serviceMethod.getArgTypes());
//                            CtMethod ctMethod = new CtMethod(classPool.get(serviceMethod.getReturnType().getName()), serviceMethod.getMethodName(), ctClasses, cc);
//                            ctMethod.setModifiers(Modifier.PUBLIC);
//                            StringBuilder body = getOverrideMethod(serviceMethod);
//                            ctMethod.setBody(body.toString());
//                            cc.addMethod(ctMethod);
                        }


                        callMethodBody.append("throw new RuntimeException();");

                        callMethodBody.append("} catch (Throwable t) { throw new RuntimeException(t); }");

                        callMethodBody.append("}");


                        cc.writeFile("D:/file");

                        cc.defrost();


                        cc.addMethod(CtNewMethod.make(callMethodBody.toString(), cc));

                        Class<?> clazz = cc.toClass();
                        ServiceWrapper proxyObject = (ServiceWrapper) clazz.getConstructor(serviceClazz).newInstance(manager.getServiceObject(service.getServiceId()));
                        proxyObjectMap.put(service.getServiceId(), proxyObject);
                        return proxyObject;
                    } catch (ClassNotFoundException e) {
                        logger.error(STR."Class of service \{service.getClassFullName()} not found !");
                        throw new BizException(STR."Class of service \{service.getClassFullName()} not found !");
                    } catch (NotFoundException e) {
                        throw new BizException(STR."The CtClass [\{service.getClassFullName()} can not be found !");
                    } catch (Exception e) {
                        logger.error("error: ", e);
                        throw new BizException(STR."Construct a client service proxy object error: \{e.getMessage()}");
                    }
                }
            }
        }
        return proxyObjectMap.get(service.getServiceId());
    }

    private String[] getMethodArgTypeNames(Class<?>[] classes) {
        return (String[]) Arrays.stream(classes).map(clazz -> clazz.getName()).toArray();
    }

    private String getArgString(String arrayName, int number, Class<?>[] argTypes) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < number; i++) {
            builder.append(STR."(\{argTypes[i].getName()})\{arrayName}[\{i}]");
            if (i < number - 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }

    private static StringBuilder getOverrideMethod(ServiceMethod serviceMethod) {
        StringBuilder body = new StringBuilder(STR."{ return (\{serviceMethod.getReturnType().getName()})$0.service.\{serviceMethod.getMethodName()}(");
        for (int i = 0; i < serviceMethod.getArgTypes().length; i++) {
            body.append(STR."$\{i + 1}");
            if (i < serviceMethod.getArgTypes().length - 1) {
                body.append(", ");
            }
        }
        body.append(");");
        return body;
    }

    private void setMethodArgTypes(CtClass[] ctClasses, Class<?>[] classes) {
        for (int i = 0; i < ctClasses.length; i++) {
            try {
                ctClasses[i] = classPool.get(classes[i].getName());
            } catch (NotFoundException e) {
                throw new BizException(STR."The CtClass [\{classes[i].getName()} can not be found !");
            }
        }
    }


    public Object getOrCreateClientServiceProxy(Class<?> clientServiceClazz, Method[] methods, Application application, ClientService clientService) {
        clientProxyNameNoMap.putIfAbsent(clientServiceClazz.getName(), new AtomicInteger());
        CtClass cc = classPool.makeClass(STR."\{clientServiceClazz.getPackageName()}.\{clientServiceClazz.getSimpleName()}Subclass\{clientProxyNameNoMap.get(clientServiceClazz.getName()).getAndIncrement()}");
        try {
            if (clientServiceClazz.isInterface()) {
                cc.setInterfaces(new CtClass[]{classPool.get(clientServiceClazz.getName())});
            } else {
                cc.setSuperclass(classPool.getCtClass(clientServiceClazz.getName()));
            }
            CtField clientInvokerParam = new CtField(classPool.get(ClientInvoker.class.getName()), "clientInvoker", cc);
            clientInvokerParam.setModifiers(Modifier.PRIVATE);
            cc.addField(clientInvokerParam);

            CtConstructor cons = new CtConstructor(new CtClass[]{classPool.get(ClientInvoker.class.getName())}, cc);
            cons.setBody("{ $0.clientInvoker = $1; }");
            cc.addConstructor(cons);

            for (ClientMethod clientMethod : clientService.getClientMethods()) {
                String methodName = clientMethod.getClientMethodName();
                Class<?>[] argTypes = clientMethod.getArgTypes();
                Class<?> returnType = clientMethod.getReturnType();
                CtClass[] ctClasses = new CtClass[argTypes.length];
                for (int i = 0; i < argTypes.length; i++) {
                    ctClasses[i] = classPool.get(argTypes[i].getName());
                }
                CtMethod ctMethod = new CtMethod(classPool.get(returnType.getName()), methodName, ctClasses, cc);
                ctMethod.setModifiers(Modifier.PUBLIC);
                String methodBody = "java.lang.Object[] args = new java.lang.Object[]{";
                for (int i = 0; i < argTypes.length; i++) {
                    methodBody = STR."\{methodBody}$\{i + 1}";
                    if (i < argTypes.length - 1) {
                        methodBody = STR."\{methodBody}, ";
                    }
                }
                methodBody = STR."\{methodBody}};";
                methodBody = STR."\{methodBody} com.wts.tsrpc.client.service.ClientMethod clientMethod = new com.wts.tsrpc.client.service.ClientMethod(); clientMethod.clientMethodName(\"\{methodName}\"); clientMethod.clientClassFullName(\"\{clientService.getClientClassFullName()}\");";
//                methodBody = STR."\{methodBody} try { clientMethod.argTypes(\{ReflectUtils.getArgsString(argTypes)}); } catch(Exception e) { throw new com.wts.tsrpc.exception.BizException(\"Class not found excpetion\"); }";
                methodBody = STR."\{methodBody} try { clientMethod.argTypes(\{ReflectUtils.getArgsString(argTypes)}); } catch(java.lang.Exception e) { throw new java.lang.RuntimeException(e); }";
                methodBody = STR."{ \{methodBody} return (\{returnType.getName()})$0.clientInvoker.invoke(args, clientMethod); }";
                ctMethod.setBody(methodBody);
                cc.addMethod(ctMethod);
            }
            cc.writeFile("log4j2_logs/");
            Class<?> clazz = cc.toClass();
            return clazz.getConstructor(ClientInvoker.class).newInstance(manager.getClientInvoker(application.getKey(), clientService.getServiceId()));
        } catch (CannotCompileException e) {
            logger.error("error: ", e);
            throw new BizException(STR."The class [\{clientServiceClazz.getName()} can not be compiled !");
        } catch (NotFoundException e) {
            throw new BizException(STR."The class [\{clientServiceClazz.getName()} can not be found !");
        } catch (Exception e) {
            logger.error("error: ", e);
            throw new BizException(STR."Construct a client service proxy object error: \{e.getMessage()}");
        }
    }

}
