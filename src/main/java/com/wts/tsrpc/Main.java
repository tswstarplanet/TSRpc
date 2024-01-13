package com.wts.tsrpc;

import com.wts.tsrpc.server.HttpServer;

public class Main {
    public static void main(String[] args) {
        HttpServer httpServer = new HttpServer();
        httpServer.start();
    }
}