package com.sinredemption.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server{

    ServerSocket serverSocket;

    public Server(int p){
        try {
            this.serverSocket = new ServerSocket(p);
            System.out.println("Server started...");
            Handler clientsHandler = new Handler();
            while (true) {
                clientsHandler.init(serverSocket.accept());
            }
        }catch (Exception e){
            System.err.println(e);
        }
    }
}
