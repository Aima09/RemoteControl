package server.yf.com.remotecontrolserver_as.localminaserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import server.yf.com.remotecontrolserver_as.config_server;

/**
 * Created by wuhuai on 2016/10/18 .
 * ;
 */

public class LocalMinaServer extends Service implements LocalMinaServerController {

    private LocalMinaCmdManager minaCmdManager;
    private LocalMinaSocketAcceptor localMinaSocketAcceptor;
    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(30);

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        minaCmdManager = LocalMinaCmdManager.getInstance();
        minaCmdManager.setLocalMinaServerController(this);
        localMinaSocketAcceptor = new LocalMinaSocketAcceptor();
        startTimerCheck();
        if (config_server.isMymachine()) {
            System.out.println("LocalMinaServer onCreate");
            start();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("LocalMinaServer onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    // 每过十分钟检测本地服务是否断开
    private void startTimerCheck() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override public void run() {
                if (!localMinaSocketAcceptor.isStart()) {
                    localMinaSocketAcceptor.close();
                    localMinaSocketAcceptor.start();
                }
            }
        }, 10 * 60 * 1000, 10 * 60 * 1000);
    }

    private void start() {
        fixedThreadPool.execute(new Runnable() {
            @Override public void run() {
                localMinaSocketAcceptor.start();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != localMinaSocketAcceptor) {
            fixedThreadPool.execute(new Runnable() {
                @Override public void run() {
                    localMinaSocketAcceptor.close();
                }
            });
        }
    }

    private Object msg;

    @Override
    public void send(Object message) {
        msg = message;
        if (null != localMinaSocketAcceptor) {
            fixedThreadPool.execute(new Runnable() {
                @Override public void run() {
                    localMinaSocketAcceptor.send(msg);
                }
            });
        }
    }

}
