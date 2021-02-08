package com.sinredemption.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class Handler {
    private ArrayList<Socket> clients;
    private ArrayList<String> clientsName;
    private int count;

    public Handler(){
        this.clients = new ArrayList<Socket>();
        this.clientsName = new ArrayList<String>();
    }

    public void init(Socket socket){
        new ClientHandler(socket, count).start();
    }

    public void HelpMsg(){
        System.out.println("@name <Username> - Change username.\n@quit - Quit chat.");
    }

    public class ClientHandler extends Thread {
        private Socket clientSocket;
        private int id;
        BufferedReader in;

        public ClientHandler(Socket socket, int count){
            this.clientSocket = socket;
            clients.add(socket);
            this.id = count;
            try {
                this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String name = in.readLine();
                if(!clientsName.contains(name))
                    clientsName.add(name);
                else{
                    int addNum;
                    for(addNum = 1; clientsName.contains(name + addNum); addNum++);
                    name = name + addNum;
                    clientsName.add(name);
                    this.id = addNum + count;
                }
                System.out.println( "Connected new user: " + name);
            }catch (Exception e){
                System.err.println(e);
            }
        }

        public void run(){
            try {
                while(true){
                String received = in.readLine();
                System.out.println("[" + clientsName.get(id) + "]: " + received);
                if(received.charAt(0) == '@'){
                    String[] cmd = received.split(" ", 3);
                    if(cmd[0].equals("@quit")) {
                        System.exit(1);
                    } else if(cmd[0].equals("@name"))
                        clientsName.set(count, cmd[1]);
                    else HelpMsg();
                }else
                    for(Socket i: clients){
                        if(i != clientSocket && i.isConnected()) {
                            DataOutputStream out = new DataOutputStream(i.getOutputStream());
                            out.flush();
                            out.writeBytes("[" + clientsName.get(id) + "]: " + received + "\n");
                        }
                    }
                }
            }catch (Exception e){
                System.err.println(e);
            }
        }
    }
}
