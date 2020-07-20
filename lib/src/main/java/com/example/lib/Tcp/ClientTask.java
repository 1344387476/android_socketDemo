package com.example.lib.Tcp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientTask extends Thread implements MsgPool.MsgCommingListener {
    private Socket mSocket;
    private InputStream is;
    private OutputStream os;

    public ClientTask(Socket socket){
        mSocket = socket;
        try {
            is = mSocket.getInputStream();
            os = mSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        while (true){
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
                try {
                  while ((line = br.readLine()) != null){
                      System.out.println("read = "+line);
                      MsgPool.getInstance().sendMsg(mSocket.getPort()+":"+line);
                  }
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }
    }

    @Override
    public void msgComming(String msg) {
        try {
            os.write(msg.getBytes());
            os.write("\n".getBytes());
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
