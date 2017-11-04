package com.yf.remotecontrolclient.httpserver;



import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

/**
 * Created by sujuntao on 2017/8/26 .
 */

public class MyHttpService extends Service {
    @Nullable @Override public IBinder onBind(Intent intent) {
        return null;
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("MyHttpService","onStartCommand");
        try {
            HttpServer mHttpServer= new HttpServer(8089);
            mHttpServer.start();
        }catch (Exception e){
            e.printStackTrace();
            Log.i("MyHttpService","启动http服务失败");
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
