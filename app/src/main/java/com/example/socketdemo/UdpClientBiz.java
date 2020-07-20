package com.example.socketdemo;

import android.os.Handler;
import android.os.Looper;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class UdpClientBiz {
    private String serverIp;
    private int serverPort;
    private InetAddress inetAddress;
    private DatagramSocket mSocket;

    private Handler handler = new Handler(Looper.getMainLooper());

    public UdpClientBiz(String serverIp,int serverPort) throws SocketException, UnknownHostException {
        this.serverIp = serverIp;
        this.serverPort = serverPort;

            mSocket = new DatagramSocket();
            inetAddress = InetAddress.getByName(this.serverIp);

    }

    public void SendMsg(final String msg, final OnMsgReturnedListenner onMsgReturnedListenner){
        new Thread(){
            @Override
            public void run() {
                try {
                    byte[] clientMsgBytes = msg.getBytes();
                    DatagramPacket clientPacket = new DatagramPacket(clientMsgBytes,clientMsgBytes.length,inetAddress,serverPort);
                    mSocket.send(clientPacket);
                    byte[] buf = new byte[1024];
                    DatagramPacket serverPacket = new DatagramPacket(buf,buf.length);
                    mSocket.receive(serverPacket);
                    final String serverMsg = new String(serverPacket.getData(),0,serverPacket.getLength());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onMsgReturnedListenner.OnMsgReturned(serverMsg);
                        }
                    });
                } catch (final Exception e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onMsgReturnedListenner.OnFailure(e);
                        }
                    });
                }
            }
        }.start();

    }

    public void onUdpDestroy(){
        if (mSocket != null) {
            mSocket.close();
        }
    }

    public interface OnMsgReturnedListenner{
        void OnMsgReturned(String msg);

        void OnFailure(Exception e);
    }

}
