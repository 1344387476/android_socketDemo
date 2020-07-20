package com.example.lib.Tcp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class MsgPool {

    private static MsgPool msgPool = new MsgPool();

    private LinkedBlockingQueue<String> mQueue = new LinkedBlockingQueue<>();

    public static MsgPool getInstance(){
        return msgPool;
    }

    private MsgPool(){

    }

    public void start(){
        new Thread(){
            @Override
            public void run() {
                while (true){
                    try {
                        String take = mQueue.take();
                        notiflyListeners(take);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }.start();
    }

    public interface MsgCommingListener{
        void msgComming(String msg);
    }

    private List<MsgCommingListener> msgCommingListeners = new ArrayList<>();

    public void addListener(MsgCommingListener msgCommingListener){
        msgCommingListeners.add(msgCommingListener);
    }

    public void notiflyListeners(String msg){
        for (MsgCommingListener listener : msgCommingListeners){
            listener.msgComming(msg);
        }
    }

    public void sendMsg(String msg){
        try {
            mQueue.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
