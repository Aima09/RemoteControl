package com.yf.remotecontrolclient.minaclient.tcp;

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
import com.yf.remotecontrolclient.util.PromptUtil;

import org.apache.mina.core.session.IoSession;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by wuhuai on 2016/10/18 .
 * ;
 */

public class MinaServer extends Service implements MinaServerController {

    private MinaMessageManager minaMessageManager;
    private MinaSocketConnector minaSocketConnector;
    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(30);

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        minaMessageManager = MinaMessageManager.getInstance();
        minaMessageManager.setMinaServerController(this);
        minaSocketConnector = new MinaSocketConnector();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MinaServer", "onStartCommand");
        connectServer();
        return super.onStartCommand(intent, flags, startId);
    }

    private void connectServer() {
        fixedThreadPool.execute(new Runnable() {
            @Override public void run() {
                PromptUtil.connectionstatus("正在连接。。。");
                boolean connectSuccess = minaSocketConnector.connectServer();
                if (connectSuccess) {
                    CmdMessage cmdMessage;
                    if (TextUtils.isEmpty(ServerDataDisposeCenter.getLocalSenderId())) {
                        CmdBean cmdBean = new CmdBean(CmdType.CMD_REGISTER, DeviceType.DEVICE_TYPE_PHONE, "");
                        cmdMessage = new CmdMessage(MessageType.MESSAGE_CMD, cmdBean);
                    } else {
                        CmdBean cmdBean = new CmdBean(CmdType.CMD_LOGIN, DeviceType.DEVICE_TYPE_PHONE, "");
                        cmdMessage = new CmdMessage(ServerDataDisposeCenter.getLocalSenderId(),"",MessageType.MESSAGE_CMD, cmdBean);
                    }
                    minaSocketConnector.send(cmdMessage);
                    //成功
                    PromptUtil.connectionstatus("连接成功");
                } else {
                    //失败
                    PromptUtil.connectionstatus("请检查网络");
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fixedThreadPool.execute(new Runnable() {
            @Override public void run() {
                minaSocketConnector.close();
            }
        });
    }

    private Object msg;

    @Override public void send(Object message) {
        msg = message;
        if (!minaSocketConnector.isConnect()) {
            connectServer();
        }
        fixedThreadPool.execute(new Runnable() {
            @Override public void run() {
                minaSocketConnector.send(msg);
            }
        });
    }

    @Override public void getSessionSend(final Object message) {
        if (!minaSocketConnector.isConnect()) {
            connectServer();
        }
        fixedThreadPool.execute(new Runnable() {
            @Override public void run() {
                IoSession session = minaSocketConnector.getSession();
                session.write(message);
            }
        });
    }

}
