package com.example.myapplication;


import android.widget.Toast;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Hook  implements IXposedHookLoadPackage {


    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {


        if (lpparam.packageName.equals("com.up366.ismart"))
        {

            Class clazz=lpparam.classLoader.loadClass("com.up366.mobile.common.utils.NetworkUtils");

            XposedHelpers.findAndHookMethod(clazz, "isWifiProxy", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    param.setResult(false);

                }
            });


        }

    }
}