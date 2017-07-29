package com.yf.remotecontrolclient;

import android.app.Application;
import android.content.Context;
import android.net.wifi.WifiManager;

public class App extends Application {
    //    public static final String TAG = App.class.getSimpleName();
    private static App instance;

    public static App getAppContext() {
        return instance;
    }

    public static App getInstance() {
        return instance;
    }

    @Override public void onCreate() {
        super.onCreate();
        WifiManager manager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        WifiManager.MulticastLock lock = manager.createMulticastLock("test wifi");
        lock.acquire();
        instance = this;
    }

}