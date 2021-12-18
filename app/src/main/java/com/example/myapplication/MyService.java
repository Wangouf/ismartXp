package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;

import java.io.IOException;

public class MyService extends Service {
    public static HTTPserver server=null;
    public MyService() {
        super();
    }
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            server = new HTTPserver();
            server.context = this.getApplicationContext();
            server.start();
            Log.d("ssss","服务启动");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}