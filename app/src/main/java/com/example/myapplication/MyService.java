package com.example.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;

import java.io.IOException;

import static android.os.Build.ID;

public class MyService extends Service {
    public static HTTPserver server=null;
    public MyService() {
        super();
    }
    @Override
    public void onCreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel("channel_1", "前台服务", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
            Notification notification = new Notification.Builder(this, ID).build();
            startForeground(1, notification);
        }
        super.onCreate();
        try {
            server = new HTTPserver();
            HTTPserver.context = this.getApplicationContext();
            server.start();
            Log.d("ssss","服务启动");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}