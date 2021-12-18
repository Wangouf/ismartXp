package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Handler {
    private static boolean isInit = false;
    private static Context context;
    private static Method getUt;
    private static Object gbCallback;
    private static Method getdID;
    private static String dID;
    Handler(Context context1, Method getUt1,Object gbCallback1,Method getdID1,String dID1) {
        if (!isInit) {
            context = context1;
            getdID = getdID1;
            gbCallback = gbCallback1;
            getUt = getUt1;
            dID = dID1;
            BroadcastReceiver receiver1 = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals("ServiceGetdID")) {
                        Intent intent2 = new Intent();
                        intent2.putExtra("RSP",dID);
                        intent2.setAction("ServiceGetdIDRSP");
                        context.sendBroadcast(intent2);
                    }
                }
            };
            BroadcastReceiver receiver2 = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals("ServiceGetUt")) {
                        String str = intent.getStringExtra("str");
                        int i = intent.getIntExtra("i",22);
                        String rsp = null;
                        try {
                            rsp = (String) getUt.invoke(this,str,i);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        Intent intent2 = new Intent();
                        intent2.putExtra("RSP",rsp);
                        intent2.setAction("ServiceGetUtRSP");
                        context.sendBroadcast(intent2);
                    }
                }
            };
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("ServiceGetUt");
            context.registerReceiver(receiver2,intentFilter);
            IntentFilter intentFilter2 = new IntentFilter();
            intentFilter2.addAction("ServiceGetdID");
            context.registerReceiver(receiver1,intentFilter2);
        }
        isInit = true;




    }

}
