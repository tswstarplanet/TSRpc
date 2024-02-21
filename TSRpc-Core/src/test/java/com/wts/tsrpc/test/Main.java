package com.wts.tsrpc.test;

public class Main {

    private ClassA classA;

    public static void main(String[] args) {
        Main main = new Main();
        main.classA = new ClassB();
        main.classA.func("abc");
    }

    public ClassA getClassA() {
        return classA;
    }

    public void setClassA(ClassA classA) {
        this.classA = classA;
    }
}
