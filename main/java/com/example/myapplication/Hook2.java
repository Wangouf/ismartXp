package com.example.myapplication;

//1332471191824bffb6f481ff4de25f55d9e29cc4143

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Hook2  implements IXposedHookLoadPackage {
    public static boolean flag = true;
    public static Method mt = null;
    public static String test = "1332471191824bffb6f481ff4de25f55d9e29cc4143";
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {


        if (lpparam.packageName.equals("com.up366.ismart"))
        {
            XposedBridge.log("Native HOOK Test........................................" +
                    "................................................" +
                    "............................");

            Class<?> clazz=lpparam.classLoader.loadClass("com.up366.common.StringUtils");
            XposedBridge.log(clazz.getName());
            Method getut = clazz.getDeclaredMethod("getUt", String.class,  int.class);
            getut.setAccessible(true);
            if (mt==null) {
                mt = getut;
            }


            XposedBridge.log("TTTTTTTTTTT::::::::::::::"+ getut.invoke(this,test, 22));
            if (flag) {
                HTTPserver.setMt(mt,this);
                new HTTPserver();
                XposedBridge.log("HTTP sever on");
                flag=false;
            }
        }

    }

}
