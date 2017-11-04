package com.yf.remotecontrolserver.common.ui;


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.yf.remotecontrolserver.common.App;

import player.yf.com.player.IAidlPlayerControllerInterface;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by sujuntao on 2017/10/19 .
 */

public class PlayerControllerManager {
    //懒汉式线程安全
    private static PlayerControllerManager instance;

    //控制接口
    private IAidlPlayerControllerInterface mIAidlPlayerControllerInterface;
    private PlayerControllerManager (){

        bindServce();
    }
    public static synchronized PlayerControllerManager getInstance() {
        if (instance == null) {
            instance = new PlayerControllerManager();
        }
        return instance;
    }

    public void onDestroy(){
        App.getAppContext().unbindService(conn);
    }

    private void bindServce() {
        //获取到服务端, 5.0 之后必须显示绑定服务
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("player.yf.com.player", "player.yf.com.player.controller.service.PlayControllerService"));
        App.getAppContext().bindService(intent, conn, BIND_AUTO_CREATE);
    }
    private ServiceConnection conn = new ServiceConnection() {
        //绑定上服务的时候
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //接受到了远程的服务
            mIAidlPlayerControllerInterface=IAidlPlayerControllerInterface.Stub.asInterface(iBinder);
        }

        // 当服务断开的时候调用
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mIAidlPlayerControllerInterface=null;
            //释放资源
            mIAidlPlayerControllerInterface=null;
            System.gc();
        }
    };

    public IAidlPlayerControllerInterface getIAidlPlayerControllerInterface() {
        if (mIAidlPlayerControllerInterface==null){
            bindServce();
        }
        return mIAidlPlayerControllerInterface;
    }
}
