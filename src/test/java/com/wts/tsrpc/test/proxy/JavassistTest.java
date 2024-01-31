package com.wts.tsrpc.test.proxy;

import javassist.*;

import java.io.IOException;

public class JavassistTest {
    public static void main(String[] args) throws NotFoundException, CannotCompileException, IOException {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.makeClass("com.wts.tsrpc.test.proxy.TestJavassist");
        CtField param = new CtField(pool.get("java.lang.String"), "name", cc);
        param.setModifiers(Modifier.PRIVATE);
        cc.addField(param, CtField.Initializer.constant("xiaoming"));
        cc.addMethod(CtNewMethod.setter("setName", param));
        cc.addMethod(CtNewMethod.getter("getName", param));

        CtConstructor cons = new CtConstructor(new CtClass[]{}, cc);
        cons.setBody("{name= \"xiaohong\";}");
        cc.addConstructor(cons);

        cons = new CtConstructor(new CtClass[]{pool.get("java.lang.String")}, cc);
        cons.setBody("$0.name = $1;");
        cc.addConstructor(cons);

        CtMethod ctMethod = new CtMethod(CtClass.voidType, "printName", new CtClass[]{}, cc);
        ctMethod.setModifiers(Modifier.PUBLIC);
        ctMethod.setBody("{System.out.println(name);}");
        cc.addMethod(ctMethod);

        cc.writeFile("log4j2_logs/");
    }
}
