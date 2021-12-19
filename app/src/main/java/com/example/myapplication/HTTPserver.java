package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class HTTPserver extends NanoHTTPD{
    private static Lock lock = new ReentrantLock();
    public static Context context;
    public static String getdID() throws InterruptedException {
        Intent intent = new Intent();
        intent.setAction("ServiceGetdID");
        HTTPserver.context.sendBroadcast(intent);
        final String[] result = {null};
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("ServiceGetdIDRSP")) {
                    result[0] = intent.getStringExtra("RSP");
                    synchronized (lock) {

                        lock.notifyAll();
                    }
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ServiceGetdIDRSP");
        context.registerReceiver(receiver,intentFilter);
        synchronized (lock) {
            while (result[0] == null) {

                lock.wait();
            }

        }
        return result[0];
    }
    public static String getUt(String s,int i) throws InterruptedException {
        Intent intent = new Intent();
        intent.setAction("ServiceGetUt");
        intent.putExtra("str",s);
        intent.putExtra("i",i);
        HTTPserver.context.sendBroadcast(intent);
        final String[] result = {null};
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("ServiceGetUtRSP")) {
                    result[0] = intent.getStringExtra("RSP");
                    synchronized (lock) {
                        //通知子线程继续走下去
                        lock.notifyAll();
                    }
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("ServiceGetUtRSP");
        context.registerReceiver(receiver,intentFilter);
        synchronized (lock) {
            while (result[0] == null) {
                //子线程等待
                lock.wait();
            }

        }
        return result[0];
    }
    HTTPserver() throws IOException {
        super(8585);
    }
    @Override
    public Response serve(IHTTPSession session) {
        String msg="Error";
        if (session.getMethod() == Method.GET) {
            Map<String, String> parms = session.getParms();
            if (parms.containsKey("method")) {
                if (parms.get("method").equals("getUt")) {
                    if (parms.get("str") != null && parms.get("i") != null) {
                        try {
                            msg = getUt(parms.get("str").toString(),22);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (parms.get("method").equals("getUniqueDeviceId")) {
                    try {
                        msg = getdID();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        return newFixedLengthResponse(msg);


    }
}
