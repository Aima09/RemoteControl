package com.yf.remotecontrolserver.intercom;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.yuanfang.intercom.output.Decoder;
import com.yuanfang.intercom.output.Tracker;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IntercomPlayService extends Service {
    private Decoder decoder;
    private Tracker tracker;
    private ExecutorService threadPool = Executors.newCachedThreadPool();

    public IntercomPlayService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        decoder = new Decoder(new Handler());
        tracker = new Tracker(new Handler());
        threadPool.execute(decoder);
        threadPool.execute(tracker);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        decoder.free();
        tracker.free();
    }
}
