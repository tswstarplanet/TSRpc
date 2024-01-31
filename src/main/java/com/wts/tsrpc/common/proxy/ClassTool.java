package com.wts.tsrpc.common.proxy;

import com.wts.tsrpc.client.ClientInvoker;
import com.wts.tsrpc.exception.BizException;
import com.wts.tsrpc.server.manage.Application;
import javassist.*;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ClassTool {
    private static final ClassPool classPool = ClassPool.getDefault();

    private static Map<String, AtomicInteger> proxyNameNoMap = new ConcurrentHashMap<>();

    private static Map<String, Object> proxyObjectMap = new ConcurrentHashMap<>();

    public static Object createClientServiceProxy(Class<?> clientServiceClazz, Method[] methods, Application application) {
        proxyNameNoMap.putIfAbsent(clientServiceClazz.getName(), new AtomicInteger());
        CtClass cc = classPool.makeClass(STR."\{clientServiceClazz.getPackageName()}.\{clientServiceClazz.getSimpleName()}Subclass\{proxyNameNoMap.get(clientServiceClazz.getName())}");
        try {
            cc.setSuperclass(classPool.getCtClass(clientServiceClazz.getName()));
            CtField clientInvokerParam = new CtField(classPool.get(ClientInvoker.class.getName()), "clientInvoker", cc);
            clientInvokerParam.setModifiers(Modifier.PRIVATE);
            cc.addField(clientInvokerParam);

            CtConstructor cons = new CtConstructor(new CtClass[]{classPool.get(ClientInvoker.class.getName())}, cc);
            cons.setBody("{ $0.clientInvoker = $1; }");
            cc.addConstructor(cons);

            return null;
        } catch (CannotCompileException e) {
            throw new BizException(STR."The class [\{clientServiceClazz.getName()} can not be compiled !");
        } catch (NotFoundException e) {
            throw new BizException(STR."The class [\{clientServiceClazz.getName()} can not be found !");
        }
    }
}
