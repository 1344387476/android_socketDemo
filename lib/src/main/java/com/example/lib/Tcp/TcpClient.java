package com.example.lib.Tcp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class TcpClient {
    private Scanner scanner;

    public TcpClient(){
        scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");
    }

    public void start(){
        try {
            Socket socket = new Socket("192.168.2.124",9090);
            final InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            final BufferedReader br = new BufferedReader(new InputStreamReader(is));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

            new Thread(){
                @Override
                public void run() {
                    String line = null;
                    try {
                    while ((line = br.readLine())!= null){
                            System.out.println(line);
                        }
                    }catch (IOException e) {

                    }
                }
            }.start();

            while (true) {
                String msg = scanner.next();
                bw.write(msg);
                bw.newLine();
                bw.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        new TcpClient().start();
    }
}
