package server.yf.com.remotecontrolserver_as.minamachines;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.yf.minalibrary.common.CmdType;
import com.yf.minalibrary.common.DeviceType;
import com.yf.minalibrary.common.MessageType;
import com.yf.minalibrary.message.CmdMessage;
import com.yf.minalibrary.message.CmdMessage.CmdBean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by wuhuai on 2016/10/18 .
 * ;
 */

public class MinaServer extends Service implements MinaServerController {

    private MinaSocketConnector minaSocketConnector;
    private MinaCmdManager minaCmdManager;
    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(30);

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        minaSocketConnector = new MinaSocketConnector();
        minaCmdManager = MinaCmdManager.getInstance();
        minaCmdManager.setMinaServerController(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MinaServer1", "onStartCommand");
        connectServer();
        return super.onStartCommand(intent, flags, startId);
    }

    private void connectServer(){
        fixedThreadPool.execute(new Runnable() {
            @Override public void run() {
                boolean connectSuccess = minaSocketConnector.connectServer();
                if (connectSuccess){
                    String cmdType;
                    if (TextUtils.isEmpty(ServerDataDisposeCenter.getLocalSenderId())){
                        cmdType = CmdType.CMD_REGISTER;
                    } else {
                        cmdType = CmdType.CMD_LOGIN;
                    }
                    CmdBean cmdBean = new CmdBean(cmdType, DeviceType.DEVICE_TYPE_PHONE,"");
                    if (cmdType.equals(CmdType.CMD_LOGIN)){
                        cmdBean.setSenderId(ServerDataDisposeCenter.getLocalSenderId());
                    }
                    CmdMessage cmdMessage = new CmdMessage(MessageType.MESSAGE_CMD, cmdBean);
                    minaSocketConnector.send(cmdMessage);
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
        fixedThreadPool.execute(new Runnable() {
            @Override public void run() {
                minaSocketConnector.send(msg);
            }
        });
    }
}
