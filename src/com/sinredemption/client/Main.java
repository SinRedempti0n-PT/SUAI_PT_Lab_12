package com.sinredemption.client;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {

    public static class MessageListener extends Thread{
        BufferedReader in;

        MessageListener(Socket socket){
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            }catch (Exception e){
                System.err.println(e);
            }
        }

        public void run(){
            try {
                while(true)
                    System.out.println(in.readLine());
            }catch (Exception e){
                System.err.println(e);
            }
        }

    }

    public static void main( String[] args ) throws UnknownHostException, IOException {
        String ip = "localhost";
        int port = 5555;

        for (int i = 0; i < args.length; i++) {
            if(args[i].equals("-ip")) {
                ip = args[++i];
            }

            if(args[i].equals("-port")) {
                port = Integer.parseInt(args[++i]);
            }
        }



        Socket clientSocket = new Socket( InetAddress.getByName(ip), port );
        DataOutputStream out = new DataOutputStream( clientSocket.getOutputStream() );
        System.out.println( "Connected to server..." );
        System.out.print("Input username: ");
        Scanner sc = new Scanner( System.in );
        out.writeBytes(sc.nextLine() + "\n");

        new MessageListener(clientSocket).start();
        boolean a = true;
        while ( a ) {
            //System.out.println( "enter data to send" );
            String msg = sc.nextLine();
            String[] cmd = msg.split(" ", 2);
            if(cmd[0].equals("@exit")){
                clientSocket.close();
                System.exit(0);
            }else
                out.writeBytes( msg + "\n" );
        }

    }
}
