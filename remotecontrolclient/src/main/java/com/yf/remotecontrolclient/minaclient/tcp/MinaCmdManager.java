package com.yf.remotecontrolclient.minaclient.tcp;


import android.util.Base64;
import android.util.Log;

import com.yf.minalibrary.common.CmdType;
import com.yf.minalibrary.common.DeviceType;
import com.yf.minalibrary.common.MessageType;
import com.yf.minalibrary.message.CmdMessage;
import com.yf.minalibrary.message.CmdMessage.CmdBean;
import com.yf.remotecontrolclient.dao.TcpAnalyzerImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuhuai on 2017/6/23 .
 * ;
 */

public class MinaCmdManager {

    private static MinaCmdManager instance;
    private MinaServerController minaServerController;

    public static synchronized MinaCmdManager getInstance() {
        if (instance == null) {
            synchronized (MinaCmdManager.class) {
                if (instance == null)
                    instance = new MinaCmdManager();
            }
        }
        return instance;
    }

    public void setMinaServerController(MinaServerController minaServerController) {
        this.minaServerController = minaServerController;
    }

    public void disposeCmd(CmdMessage cmdMessage) {
        CmdBean cmdBean = cmdMessage.getCmdBean();
        String cmdType = cmdBean.getCmdType();
        switch (cmdType) {
            case CmdType.CMD_REGISTER:
                String uuid = cmdBean.getCmdContent();
                ServerDataDisposeCenter.setLocalSenderId(uuid);
                Log.d("MinaCmdManager", uuid);
                Log.d("IoClientHandler", uuid);
                break;
            case CmdType.CMD_LOGIN:
                String loginResult = cmdBean.getCmdContent();
                Log.d("MinaCmdManager", loginResult);
                break;
            case CmdType.CMD_HEARTBEAT:
                Log.d("MinaCmdManager", "我的对应的远程服务 session 还在线。。。。。。");
                break;
            case CmdType.CMD_MUSIC:
                TcpAnalyzerImpl.getInstans().analy(cmdBean.getCmdContent().getBytes());
                Log.d("MinaCmdManager", "接收到音乐命令：" + cmdBean.getCmdContent());
                break;
        }
    }

    public void sendControlCmd(String cmdContent) {
        if (null != minaServerController) {
            CmdBean cmdBean = new CmdBean(CmdType.CMD_MUSIC, DeviceType.DEVICE_TYPE_PHONE, cmdContent);
            CmdMessage cmdMessage = new CmdMessage(ServerDataDisposeCenter.getLocalSenderId(),
                    ServerDataDisposeCenter.getRemoteReceiverId(), MessageType.MESSAGE_CMD, cmdBean);
            minaServerController.send(cmdMessage);
        }
    }

    /**
     * 对讲所用发送的MINA接口
     *
     * @param content byte[]
     */
    public void sendIntercomContent(byte[] content) {
        String cmdContent = Base64.encodeToString(content, Base64.DEFAULT);
        sendControlCmd(cmdContent);
//        if (null != minaServerController) {
//            IntercomBean intercomBean = new IntercomBean(ServerDataDisposeCenter.getLocalSenderId(),
//                    ServerDataDisposeCenter.getRemoteReceiverId(),content);
//            IntercomMessage intercomMessage = new IntercomMessage(MessageType.MESSAGE_INTERCOM, intercomBean);
//            minaServerController.send(intercomMessage);
//        }
    }

}
