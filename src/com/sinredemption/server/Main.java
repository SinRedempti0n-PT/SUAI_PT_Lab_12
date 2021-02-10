package com.sinredemption.server;


public class Main {

    public static void main(String[] args) {
        int port = 5555;

        for (int i = 0; i < args.length; i++) {
            if(args[i].equals("-port")) {
                port = Integer.parseInt(args[++i]);
                break;
            }
        }
        new Server(port);
    }
}
