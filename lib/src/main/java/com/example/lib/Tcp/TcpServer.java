package com.example.lib.Tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {

    public void start(){
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(9090);
            MsgPool.getInstance().start();

            while (true){
               Socket socket = serverSocket.accept();
               System.out.println("IP = "+socket.getInetAddress().getHostAddress()+"  port:"+socket.getPort()+" is online...");

                  ClientTask clientTask = new ClientTask(socket);
                  MsgPool.getInstance().addListener(clientTask);
                  clientTask.start();

           }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        new TcpServer().start();
    }
}