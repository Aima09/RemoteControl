package com.yf.remotecontrolclient.minaclient.tcp;


import android.util.Base64;
import android.util.Log;

import com.yf.minalibrary.common.CmdType;
import com.yf.minalibrary.common.DeviceType;
import com.yf.minalibrary.common.FileHelper;
import com.yf.minalibrary.common.MessageType;
import com.yf.minalibrary.message.BaseMessage;
import com.yf.minalibrary.message.CmdMessage;
import com.yf.minalibrary.message.FileMessage;
import com.yf.minalibrary.message.IntercomMessage;
import com.yf.remotecontrolclient.dao.TcpAnalyzerImpl;

import java.io.File;
import java.io.IOException;

/**
 * Created by wuhuai on 2017/6/23 .
 * ;
 */

public class MinaMessageManager {

    private static final String TAG ="MinaMessageManager" ;
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
        String cmdType= cmdMessage.getCmdType();
        switch (cmdType) {
            case CmdType.CMD_REGISTER:
                String uuid = cmdMessage.getCmdContent();
                ServerDataDisposeCenter.setLocalSenderId(uuid);
                Log.d("MinaMessageManager", uuid);
                Log.d("IoClientHandler", uuid);
                break;
            case CmdType.CMD_LOGIN:
                String loginResult = cmdMessage.getCmdContent();
                Log.d("MinaMessageManager", loginResult);
                break;
            case CmdType.CMD_HEARTBEAT:
                Log.d("MinaMessageManager", "我的对应的远程服务 session 还在线。。。。。。");
                break;
            case CmdType.CMD_MUSIC:
                TcpAnalyzerImpl.getInstans().analy(cmdMessage.getCmdContent().getBytes());
                Log.d("MinaMessageManager", "接收到音乐命令：" + cmdMessage.getCmdContent());
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
        CmdMessage cmdMessage = new CmdMessage(ServerDataDisposeCenter.getLocalSenderId(),
                ServerDataDisposeCenter.getRemoteReceiverId(), MessageType.MESSAGE_CMD, cmdType,DeviceType.DEVICE_TYPE_PHONE,cmdContent);
        send(cmdMessage);
    }

    public void sendFile(String filePath,String use){
        String fileName=null;
        int fileSize=0;
        byte[] fileContent=null;
        File file = new File(filePath);
        if (file.exists()){
            FileHelper helper = new FileHelper();
            fileName = file.getName();
            fileSize = (int) file.length();
            try {
                fileContent = helper.getContent(file);
                Log.i(TAG,"filecontent"+fileContent.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileMessage fileMessage = new FileMessage(ServerDataDisposeCenter.getLocalSenderId(),
                ServerDataDisposeCenter.getRemoteReceiverId(),MessageType.MESSAGE_FILE,fileName,fileSize,fileContent,use);
        Log.i(TAG,fileMessage.toString());
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
        IntercomMessage intercomMessage = new IntercomMessage(ServerDataDisposeCenter.getLocalSenderId(),
                ServerDataDisposeCenter.getRemoteReceiverId(),MessageType.MESSAGE_INTERCOM, cmdContent);
        send(intercomMessage);
    }

}
