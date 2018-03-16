/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package httpserver;

import java.net.*;
import java.io.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Fabian Ardila
 */
public class HttpServer {

    static String fileName;
    static HandleRequest hr;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        serverSocket = new ServerSocket(/*new Integer(System.getenv("port"))*/36000);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10));
        while (true) {
            executor.execute(new HandleRequest(serverSocket.accept()));
            System.out.println("Socket closed");
        }

    }
}
