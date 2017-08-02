package com.yf.remotecontrolclient.minaclient.tcp;


import android.util.Base64;
import android.util.Log;

import com.yf.minalibrary.common.CmdType;
import com.yf.minalibrary.common.DeviceType;
import com.yf.minalibrary.common.MessageType;
import com.yf.minalibrary.message.BaseMessage;
import com.yf.minalibrary.message.CmdMessage;
import com.yf.minalibrary.message.CmdMessage.CmdBean;
import com.yf.minalibrary.message.FileMessage;
import com.yf.minalibrary.message.FileMessage.FileBean;
import com.yf.minalibrary.message.IntercomMessage;
import com.yf.minalibrary.message.IntercomMessage.IntercomBean;
import com.yf.remotecontrolclient.dao.TcpAnalyzerImpl;

/**
 * Created by wuhuai on 2017/6/23 .
 * ;
 */

public class MinaMessageManager {

    private static MinaMessageManager instance;
    private MinaServerController minaServerController;

    public static synchronized MinaMessageManager getInstance() {
        if (instance == null) {
            synchronized (MinaMessageManager.class) {
                if (instance == null)
                    instance = new MinaMessageManager();
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
                Log.d("MinaMessageManager", uuid);
                Log.d("IoClientHandler", uuid);
                break;
            case CmdType.CMD_LOGIN:
                String loginResult = cmdBean.getCmdContent();
                Log.d("MinaMessageManager", loginResult);
                break;
            case CmdType.CMD_HEARTBEAT:
                Log.d("MinaMessageManager", "我的对应的远程服务 session 还在线。。。。。。");
                break;
            case CmdType.CMD_MUSIC:
                TcpAnalyzerImpl.getInstans().analy(cmdBean.getCmdContent().getBytes());
                Log.d("MinaMessageManager", "接收到音乐命令：" + cmdBean.getCmdContent());
                break;
        }
    }

    public void send(BaseMessage baseMessage){
        if (null != minaServerController) {
            minaServerController.send(baseMessage);
        }
    }

    public void sendControlCmd(String cmdContent) {
        sendControlCmd(CmdType.CMD_MUSIC,cmdContent);
    }

    public void sendControlCmd(String cmdType, String cmdContent) {
        CmdBean cmdBean = new CmdBean(cmdType, DeviceType.DEVICE_TYPE_PHONE, cmdContent);
        CmdMessage cmdMessage = new CmdMessage(ServerDataDisposeCenter.getLocalSenderId(),
                ServerDataDisposeCenter.getRemoteReceiverId(), MessageType.MESSAGE_CMD, cmdBean);
        send(cmdMessage);
    }

    public void sendFile(String filePath){
        FileBean bean = new FileBean(filePath);
        FileMessage fileMessage = new FileMessage(ServerDataDisposeCenter.getLocalSenderId(),
                ServerDataDisposeCenter.getRemoteReceiverId(),MessageType.MESSAGE_FILE,bean);
        if (null != minaServerController) {
            minaServerController.getSessionSend(fileMessage);
        }
    }
    /**
     * 对讲所用发送的MINA接口
     *
     * @param content byte[]
     */
    public void sendIntercomContent(byte[] content) {
        String cmdContent = Base64.encodeToString(content, Base64.DEFAULT);
        IntercomBean intercomBean = new IntercomBean(cmdContent);
        IntercomMessage intercomMessage = new IntercomMessage(ServerDataDisposeCenter.getLocalSenderId(),
                ServerDataDisposeCenter.getRemoteReceiverId(),MessageType.MESSAGE_INTERCOM, intercomBean);
        send(intercomMessage);
    }

}
