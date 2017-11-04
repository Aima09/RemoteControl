package com.yf.remotecontrolserver.common;


import android.os.Build;

public class config_server {
    public static final String TAG = "config_server";
    public static String room_no="110";
    public static boolean isMymachine() {
//		Log.i(TAG,"DEVICE="+android.os.Build.DEVICE);
//		Log.d(TAG, "Build.SERIAL = " + Build.SERIAL);
        return "BM206".equals(Build.DEVICE);
    }
}
