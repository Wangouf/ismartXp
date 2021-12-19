package com.example.myapplication;


import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Hook implements IXposedHookLoadPackage {
    private static Context ctx;
    private static Method getUtMethod;//static (String,int)
    private static Object gbCallBack;//to call getUniqueDeviceId
    private static Method getUniqueDeviceId;//(void)
    private static String dID;
    private static void getContext() {
        XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                if (param.args[0] instanceof Context) {
                    Context context = (Context) param.args[0];
                    ctx = context;
                    Intent intent = new Intent();
                    intent.setClassName("com.example.myapplication","com.example.myapplication.MyService");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        ctx.startForegroundService(intent);
                        new Handler(ctx,getUtMethod,gbCallBack,getUniqueDeviceId,dID);
                    } else {
                        ctx.startService(intent);
                        new Handler(ctx,getUtMethod,gbCallBack,getUniqueDeviceId,dID);
                    }
                } else {
                    XposedBridge.log("get Context Failllllllll");
                }

            }
        });
    }
    private static void getUtMethod(XC_LoadPackage.LoadPackageParam lpparam) throws ClassNotFoundException, NoSuchMethodException {
        Class<?> clazz3 = lpparam.classLoader.loadClass("com.up366.common.StringUtils");
        Method getut = clazz3.getDeclaredMethod("getUt", String.class, int.class);
        getut.setAccessible(true);
        Hook.getUtMethod = getut;
    }
    private static void getUniqueDeviceIdMethod(XC_LoadPackage.LoadPackageParam lpparam) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException {
        Class<?> clazz2 = lpparam.classLoader.loadClass("com.up366.mobile.Up366Application");
        Object instance = clazz2.newInstance();
        Field get = clazz2.getDeclaredField("gbCallBack");
        get.setAccessible(true);
        Object gbCall = get.get(instance);
        Method getUniqueDeviceId = get.getType().getDeclaredMethod("getUniqueDeviceId");
        Hook.gbCallBack = gbCall;
        Hook.getUniqueDeviceId = getUniqueDeviceId;
//        String Did = (String) getUniqueDeviceId.invoke(gbCall);
    }
    private static void AllowProxy(XC_LoadPackage.LoadPackageParam lpparam) throws ClassNotFoundException {
        Class clazz = lpparam.classLoader.loadClass("com.up366.mobile.common.utils.NetworkUtils");
        XposedHelpers.findAndHookMethod(clazz, "isWifiProxy", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                param.setResult(false);
            }
        });
    }
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (lpparam.packageName.equals("com.up366.ismart")) {
            AllowProxy(lpparam);
            getUniqueDeviceIdMethod(lpparam);
            getUtMethod(lpparam);
            dID = (String) getUniqueDeviceId.invoke(gbCallBack);
            getContext();


        }

    }
}
