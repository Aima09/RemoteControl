package com.yf.remotecontrolserver.common;

import android.app.Application;
 
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
        instance = this;
    }

}