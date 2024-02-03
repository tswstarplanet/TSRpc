package com.wts.tsrpc.common.proxy;

import com.wts.tsrpc.client.ClientInvoker;
import com.wts.tsrpc.client.ClientService;
import com.wts.tsrpc.exception.BizException;
import com.wts.tsrpc.server.manage.Application;
import com.wts.tsrpc.server.manage.Manager;
import javassist.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ClassTool {
    private static final Logger logger = LoggerFactory.getLogger(ClassTool.class);

    private static final ClassPool classPool = ClassPool.getDefault();

    private static Map<String, AtomicInteger> proxyNameNoMap = new ConcurrentHashMap<>();

    private static Map<String, Object> proxyObjectMap = new ConcurrentHashMap<>();

    private Manager manager;

    public ClassTool manager(Manager manager) {
        this.manager = manager;
        return this;
    }

    public Object createClientServiceProxy(Class<?> clientServiceClazz, Method[] methods, Application application, ClientService clientService) {
        proxyNameNoMap.putIfAbsent(clientServiceClazz.getName(), new AtomicInteger());
        CtClass cc = classPool.makeClass(STR."\{clientServiceClazz.getPackageName()}.\{clientServiceClazz.getSimpleName()}Subclass\{proxyNameNoMap.get(clientServiceClazz.getName()).getAndIncrement()}");
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

            for (Method method : methods) {
                String methodName = method.getName();
                Class<?>[] argTypes = method.getParameterTypes();
                Class<?> returnType = method.getReturnType();
                CtClass[] ctClasses = new CtClass[argTypes.length];
                for (int i = 0; i < argTypes.length; i++) {
                    ctClasses[i] = classPool.get(argTypes[i].getName());
                }
                CtMethod ctMethod = new CtMethod(classPool.get(returnType.getName()), methodName, ctClasses, cc);
                ctMethod.setModifiers(Modifier.PUBLIC);
                String arguments = "Object[] args = new Object[]{";
                for (int i = 0; i < argTypes.length; i++) {
                    arguments = STR."\{arguments}$\{i + 1}";
                    if (i < argTypes.length - 1) {
                        arguments = STR."\{arguments}, ";
                    }
                }
                arguments = STR."\{arguments}};";
                String methodBody = STR."{ \{arguments} return (\{returnType.getName()})$0.clientInvoker.invoke(args); }";
                ctMethod.setBody(methodBody);
                cc.addMethod(ctMethod);
            }
            cc.writeFile("log4j2_logs/");
            Class<?> clazz = cc.toClass();
            return clazz.getConstructor(ClientInvoker.class).newInstance(manager.getClientInvoker(application.getKey(), clientService.getServiceId()));
        } catch (CannotCompileException e) {
            throw new BizException(STR."The class [\{clientServiceClazz.getName()} can not be compiled !");
        } catch (NotFoundException e) {
            throw new BizException(STR."The class [\{clientServiceClazz.getName()} can not be found !");
        } catch (Exception e) {
            logger.error("error: ", e);
            throw new BizException(STR."Construct a client service proxy object error: \{e.getMessage()}");
        }
    }
}
