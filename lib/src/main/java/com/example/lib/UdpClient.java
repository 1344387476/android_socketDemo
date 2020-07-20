package com.example.lib;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UdpClient {
    private String serverIp = "192.168.2.124";
    private int serverPort = 7777;
    private InetAddress inetAddress;
    private DatagramSocket mSocket;
    private Scanner scanner;

    public UdpClient(){
        try {
            mSocket = new DatagramSocket();
            scanner = new Scanner(System.in);
            scanner.useDelimiter("\n");
            inetAddress = InetAddress.getByName(serverIp);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start(){
        while (true){
            try {
                String clientMsg = scanner.next();
                byte[] clientMsgBytes = clientMsg.getBytes();
                DatagramPacket clientPacket = new DatagramPacket(clientMsgBytes,clientMsgBytes.length,inetAddress,serverPort);
                mSocket.send(clientPacket);
                byte[] buf = new byte[1024];
                DatagramPacket serverPacket = new DatagramPacket(buf,buf.length);
                mSocket.receive(serverPacket);
                String serverMsg = new String(serverPacket.getData(),0,serverPacket.getLength());
                System.out.println("Msg:"+serverMsg);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args){
        new UdpClient().start();
    }

}
