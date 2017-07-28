package com.yf.remotecontrolserver.remoteminaclient;


import android.util.Log;

import com.yf.minalibrary.common.CmdType;
import com.yf.minalibrary.common.DeviceType;
import com.yf.minalibrary.common.MessageType;
import com.yf.minalibrary.message.CmdMessage;
import com.yf.minalibrary.message.CmdMessage.CmdBean;
import com.yf.remotecontrolserver.dao.TcpAnalyzerImpl;
import com.yuanfang.intercom.data.AudioData;
import com.yuanfang.intercom.data.MessageQueue;

import java.util.Arrays;


/**
 * Created by wuhuai on 2017/6/23 .
 * ;
 */

public class ClientMinaCmdManager {
    private static final String TAG = "ClientMinaCmdManager";
    private static ClientMinaCmdManager instance;
    private ClientMinaServerController clientMinaServerController;
    private MinaCmdCallBack minaCmdCallBack;

    public interface MinaCmdCallBack {
        void minaCmdCallBack(Object message);
    }

    public static synchronized ClientMinaCmdManager getInstance() {
        if (instance == null) {
            synchronized (ClientMinaCmdManager.class) {
                if (instance == null)
                    instance = new ClientMinaCmdManager();
            }
        }
        return instance;
    }

    public MinaCmdCallBack getMinaCmdCallBack() {
        return minaCmdCallBack;
    }

    public void setMinaCmdCallBack(MinaCmdCallBack minaCmdCallBack) {
        this.minaCmdCallBack = minaCmdCallBack;
    }

    public void exeMinaCmdCallBack(Object message) {
        if (null != minaCmdCallBack) {
            minaCmdCallBack.minaCmdCallBack(message);
        }
    }

    public void setClientMinaServerController(ClientMinaServerController clientMinaServerController) {
        this.clientMinaServerController = clientMinaServerController;
    }

    public void disposeCmd(CmdMessage cmdMessage) {
        CmdBean cmdBean = cmdMessage.getCmdBean();
        String cmdType = cmdBean.getCmdType();
        Log.d("ClientMinaCmdManager", "CMD_BUSINESS" + cmdBean.getCmdContent());
        switch (cmdType) {
            case CmdType.CMD_REGISTER:
                String uuid = cmdBean.getCmdContent();
                ClientDataDisposeCenter.setLocalSenderId(uuid);
                Log.d("ClientMinaCmdManager", uuid);
                Log.d("IoClientHandler", uuid);
                exeMinaCmdCallBack(uuid);
                break;
            case CmdType.CMD_LOGIN:
                String loginResult = cmdBean.getCmdContent();
                Log.d("ClientMinaCmdManager", loginResult);
                exeMinaCmdCallBack(ClientDataDisposeCenter.getLocalSenderId());
                break;
            case CmdType.CMD_HEARTBEAT:
                Log.d("ClientMinaCmdManager", "我的对应的远程服务 session 还在线。。。。。。");
                break;
            case CmdType.CMD_MUSIC:
                TcpAnalyzerImpl.getInstans().analy(cmdBean.getCmdContent().getBytes(), cmdBean.getSenderId());
                Log.d("ClientMinaCmdManager", "CMD_MUSIC" + cmdBean.getCmdContent());
                Log.d("ClientMinaCmdManager", "rid" + cmdBean.getReceiverId());
                break;
            case CmdType.CMD_INTERCOM:
                Log.d(TAG, "disposeCmd: " + cmdType);
                AudioData audioData = new AudioData(Arrays.copyOf(cmdBean.getCmdContent().getBytes(),
                        cmdBean.getCmdContent().getBytes().length));
                MessageQueue.getInstance(MessageQueue.DECODER_DATA_QUEUE).put(audioData);
                break;
        }
    }

    public void sendControlCmd(String cmdContent, String receiverId) {
        if (null != clientMinaServerController) {
            CmdBean cmdBean = new CmdBean(ClientDataDisposeCenter.getLocalSenderId(),
                    receiverId, CmdType.CMD_MUSIC, DeviceType.DEVICE_TYPE_PHONE, cmdContent);

            CmdMessage cmdMessage = new CmdMessage(MessageType.MESSAGE_CMD, cmdBean);
            clientMinaServerController.send(cmdMessage);
        }
    }

}
