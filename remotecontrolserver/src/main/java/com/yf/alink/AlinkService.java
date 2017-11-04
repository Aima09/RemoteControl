package com.yf.alink;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class AlinkService extends Service {
    public AlinkService() {
    }
    IBinder mIBinder;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void startAlink(){
        System.out.println("startAlink!");
    }

    public void stopAlink(){
        System.out.println("stopAlink!");
    }

    //定义中间人对象(IBander)
    private class MyBinder extends Binder implements IlinkService{
        //办证
        public void callStartAlink(){
            startAlink();
        }
        //打麻将
        public void callstopAlink(){
            stopAlink();
        }

    }

}
