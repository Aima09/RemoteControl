package server.yf.com.remotecontrolserver_as.remoteminaclient;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.yf.minalibrary.common.CmdType;
import com.yf.minalibrary.common.DeviceType;
import com.yf.minalibrary.common.MessageType;
import com.yf.minalibrary.message.CmdMessage;
import com.yf.minalibrary.message.CmdMessage.CmdBean;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import server.yf.com.remotecontrolserver_as.config_server;


/**
 * Created by wuhuai on 2016/10/18 .
 * ;
 */

public class ClientMinaServer extends Service implements ClientMinaServerController {

    private ClientMinaCmdManager clientMinaCmdManager;
    private ClientMinaSocketConnector clientMinaSocketConnector;
    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(30);

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        clientMinaSocketConnector = new ClientMinaSocketConnector();
        clientMinaCmdManager = ClientMinaCmdManager.getInstance();
        clientMinaCmdManager.setClientMinaServerController(this);
        startTimerCheck();
        if(config_server.isMymachine()){
            System.out.println("ClientMinaServer onCreate");
            connectServer();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("ClientMinaServer onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    // 每过十分钟检测远程客户端是否断开，断开则重连
    private void startTimerCheck() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override public void run() {
                if (!clientMinaSocketConnector.isConnect()){
                    connectServer();
                }
            }
        }, 10 * 60 * 1000, 10 * 60 * 1000);
    }

    private void connectServer(){
        fixedThreadPool.execute(new Runnable() {
            @Override public void run() {
                boolean connectSuccess = clientMinaSocketConnector.connectServer();
                if (connectSuccess){
                    String cmdType;
                    if (TextUtils.isEmpty(ClientDataDisposeCenter.getLocalSenderId())){
                        cmdType = CmdType.CMD_REGISTER;
                    } else {
                        cmdType = CmdType.CMD_LOGIN;
                    }
                    CmdBean cmdBean = new CmdBean(cmdType, DeviceType.DEVICE_TYPE_PHONE,"");
                    if (cmdType.equals(CmdType.CMD_LOGIN)){
                        cmdBean.setSenderId(ClientDataDisposeCenter.getLocalSenderId());
                    }
                    CmdMessage cmdMessage = new CmdMessage(MessageType.MESSAGE_CMD, cmdBean);
                    clientMinaSocketConnector.send(cmdMessage);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fixedThreadPool.execute(new Runnable() {
            @Override public void run() {
                clientMinaSocketConnector.close();
            }
        });
    }

    private Object msg;
    @Override public void send(Object message) {
        msg = message;
        if (!clientMinaSocketConnector.isConnect()){
            connectServer();
        }
        fixedThreadPool.execute(new Runnable() {
            @Override public void run() {
                clientMinaSocketConnector.send(msg);
            }
        });
    }
}
