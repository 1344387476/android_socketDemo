package com.example.lib;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class UdpServer {
    private InetAddress mInetAddress;
    private int mPort = 7777;
    private DatagramSocket mSocket;
    private Scanner scanner;

    public UdpServer(){
        try {
            mInetAddress = InetAddress.getLocalHost();
            mSocket = new DatagramSocket(mPort,mInetAddress);
            scanner = new Scanner(System.in);
            scanner.useDelimiter("\n");

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void Start(){
        while (true){
            try {
                byte[] buf = new byte[1024];
                DatagramPacket datagramPacket = new DatagramPacket(buf,buf.length);
                mSocket.receive(datagramPacket);
                InetAddress address = datagramPacket.getAddress();
                int port = datagramPacket.getPort();
                String msg = new String(datagramPacket.getData(),0,datagramPacket.getLength());
                System.out.println("IP:"+address+" Port:"+port+"\n"+"msg:   "+msg);
                String returnedMsg = scanner.next();
                byte[] returnedMsgBytes = returnedMsg.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(returnedMsgBytes,returnedMsgBytes.length,datagramPacket.getSocketAddress());
                mSocket.send(sendPacket);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args){
        new UdpServer().Start();
    }
}
