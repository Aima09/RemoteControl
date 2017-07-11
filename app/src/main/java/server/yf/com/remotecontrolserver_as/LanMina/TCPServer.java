package server.yf.com.remotecontrolserver_as.LanMina;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import server.yf.com.remotecontrolserver_as.config_server;

/**
 * Created by wuhuai on 2016/10/18 .
 * ;
 */

public class TCPServer extends Service implements MinaServerController{

    private MinaSocketConnector minaSocketConnector;
    private LanMinaCmdManager minaCmdManager;
    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(30);

    private CallBack callBack;

    /**
     * The entry point.
     */
//    public static void main(String[] args) throws IOException {
//        new TCPServer().startServer();
//    }
    public void exeCallBack(String message) {
        if (callBack != null) {
            callBack.callBack(message);
            System.out.println("exeCallBack " + message);
        }
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public boolean startServer() {
        return minaSocketConnector.start();
    }


    public interface CallBack {
        void callBack(String message);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        minaSocketConnector = new MinaSocketConnector();
        minaCmdManager = LanMinaCmdManager.getInstance();
        minaCmdManager.setMinaServerController(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("onStartCommand");
        if(config_server.isMymachine()){
            start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != minaSocketConnector){
            fixedThreadPool.execute(new Runnable() {
                @Override public void run() {
                    minaSocketConnector.close();
                }
            });
        }
    }
    @Override
    public void send(final Object message){
        if (null != minaSocketConnector){
            fixedThreadPool.execute(new Runnable() {
                @Override public void run() {
                    minaSocketConnector.send(message);
                }
            });
        }
    }
    @Override
    public void start(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("start1");
                minaSocketConnector.start();
            }
        }).start();
    }
    @Override
    public void close(){
        if (null != minaSocketConnector){
            minaSocketConnector.close();
        }
    }
}
