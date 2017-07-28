package com.yuanfang.intercom.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.yuanfang.intercom.service.IIntercomService;
import com.yuanfang.intercom.service.IntercomService;

/**
 * Created by xuie on 17-7-13.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    public abstract void serviceConnected();

    public abstract void serviceDisconnected();

    /**
     * onServiceConnected和onServiceDisconnected运行在UI线程中
     */
    protected IIntercomService intercomService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            intercomService = IIntercomService.Stub.asInterface(service);
            serviceConnected();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");
//            serviceDisconnected();
            intercomService = null;
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        // 启动Service
        Intent intent = new Intent(this, IntercomService.class);
        startService(intent);
        initAudioManager();
    }

    /**
     * 初始化AudioManager配置
     */
    private void initAudioManager() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        audioManager.setSpeakerphoneOn(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        Intent intent = new Intent(this, IntercomService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    public void onBackPressed() {
        // 发送离开群组消息
        try {
            intercomService.leaveGroup();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if (intercomService != null && intercomService.asBinder().isBinderAlive()) {
            serviceDisconnected();
            unbindService(serviceConnection);
        }
        // 停止Service
        Intent intent = new Intent(this, IntercomService.class);
        stopService(intent);
    }

}
