package com.example.myapplication;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import de.robv.android.xposed.IXposedHookLoadPackage;
import fi.iki.elonen.NanoHTTPD;

public class HTTPserver extends NanoHTTPD{
    public static java.lang.reflect.Method mt;
    public static Hook2 xpi;
    public static void setMt(java.lang.reflect.Method mtt, Hook2 xpii) {
        mt=mtt;
        xpi=xpii;
    }
    HTTPserver() throws IOException {
        super(8585);
    }
    @Override
    public Response serve(IHTTPSession session) {
        String msg="Error";
        if (session.getMethod() == Method.GET) {
            Map<String, String> parms = session.getParms();
            if (parms.get("str") != null && parms.get("i") != null) {
                try {
                    msg = (String) mt.invoke(this, parms.get("str").toString(), Integer.valueOf(parms.get("i").toString()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return newFixedLengthResponse(msg);


    }
}
