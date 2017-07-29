package com.yf.remotecontrolclient.minaclient.tcp;

import android.content.Intent;

import com.yf.remotecontrolclient.App;

/**
 * Created by wuhuai on 2017/6/26 .
 * ;
 */

public class RemoteServerManager {
    private static RemoteServerManager instance;

    public static synchronized RemoteServerManager getInstance() {
        if (instance == null) {
            synchronized (RemoteServerManager.class) {
                if (instance == null)
                    instance = new RemoteServerManager();
            }
        }
        return instance;
    }

    public void startRemoteServer() {
        App.getAppContext().startService(new Intent(App.getAppContext(), MinaServer.class));
    }

    public void stopRemoteServer() {
        App.getAppContext().stopService(new Intent(App.getAppContext(), MinaServer.class));
    }

    public void reStartRemoteServer() {
        stopRemoteServer();
        startRemoteServer();
    }
}
