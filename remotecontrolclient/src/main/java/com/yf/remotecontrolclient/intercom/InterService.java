package com.yf.remotecontrolclient.intercom;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class InterService extends Service {
    private static final String TAG = "InterService";
    private ExecutorService threadPool = Executors.newCachedThreadPool();
    private Recorder recorder;

    @Override
    public void onCreate() {
        super.onCreate();
        recorder = new Recorder(new Handler());
        threadPool.execute(recorder);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getBooleanExtra("record", false)) {
            if (!recorder.isRecording()) {
                recorder.setRecording(true);
                threadPool.execute(recorder);
            }
        } else {
            if (recorder.isRecording()) {
                recorder.setRecording(false);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recorder.free();
        stopSelf();
    }
}
