package server.yf.com.remotecontrolserver_as.minaserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by sujuntao on 2017/6/27.
 */

public class MinaLoginService extends Service{
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags,   int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
