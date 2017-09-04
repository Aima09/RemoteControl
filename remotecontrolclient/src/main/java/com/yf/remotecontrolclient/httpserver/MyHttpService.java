package com.yf.remotecontrolclient.httpserver;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;


import java.io.IOException;

/**
 * Created by sujuntao on 2017/8/26 .
 */

public class MyHttpService extends Service{
    @Nullable @Override public IBinder onBind(Intent intent) {
        return null;
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            HttpServer mHttpServer= new HttpServer(8089);
            try {
                mHttpServer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
