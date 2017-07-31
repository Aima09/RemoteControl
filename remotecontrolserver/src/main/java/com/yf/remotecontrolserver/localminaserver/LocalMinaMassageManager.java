package com.yf.remotecontrolserver.localminaserver;


import android.util.Base64;
import android.util.Log;

import com.yf.minalibrary.common.CmdType;
import com.yf.minalibrary.common.DeviceType;
import com.yf.minalibrary.common.MessageType;
import com.yf.minalibrary.message.CmdMessage;
import com.yf.minalibrary.message.CmdMessage.CmdBean;
import com.yf.minalibrary.message.IntercomMessage;
import com.yf.minalibrary.message.IntercomMessage.IntercomBean;
import com.yf.remotecontrolserver.dao.TcpAnalyzerImpl;
import com.yuanfang.intercom.data.AudioData;
import com.yuanfang.intercom.data.MessageQueue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wuhuai on 2017/6/23 .
 * ;
 */

public class LocalMinaMassageManager {

    private static LocalMinaMassageManager instance;
    private LocalMinaServerController localMinaServerController;

    public static synchronized LocalMinaMassageManager getInstance() {
        if (instance == null) {
            synchronized (LocalMinaMassageManager.class) {
                if (instance == null)
                    instance = new LocalMinaMassageManager();
            }
        }
        return instance;
    }

    public void setLocalMinaServerController(LocalMinaServerController localMinaServerController) {
        this.localMinaServerController = localMinaServerController;
    }

    public void disposeCmd(CmdMessage cmdMessage) {
        CmdBean cmdBean = cmdMessage.getCmdBean();
        String cmdType = cmdBean.getCmdType();
        switch (cmdType) {
            case CmdType.CMD_MUSIC:
                Log.d("MinaCmdManager", "接收到音乐命令：" + cmdBean.getCmdContent());
                TcpAnalyzerImpl.getInstans().analy(cmdBean.getCmdContent().getBytes(), null);
                break;
            case CmdType.CMD_SECURITY:
                break;
        }
    }

    public void disposeIntercom(IntercomMessage intercomMessage) {
        IntercomBean intercomBean = intercomMessage.getIntercomBean();
        Log.d("LocalMinaMassageManager", "MessageQueue.getInstance " + Arrays.toString(
                Base64.decode(intercomBean.getIntercomContent(),Base64.DEFAULT)));
        AudioData audioData = new AudioData(Base64.decode(intercomBean.getIntercomContent(),Base64.DEFAULT));
        MessageQueue.getInstance(MessageQueue.DECODER_DATA_QUEUE).put(audioData);
    }

    public void sendControlCmd(String cmdContent) {
        sendControlCmd(CmdType.CMD_MUSIC,cmdContent);
    }

    public void sendControlCmd(String cmdType,String cmdContent) {
        if (null != localMinaServerController) {
            Log.i("LocalMinaMassageManager", "发送数据");
            CmdBean cmdBean = new CmdBean(cmdType, DeviceType.DEVICE_TYPE_IPAD, cmdContent);
            CmdMessage cmdMessage = new CmdMessage(MessageType.MESSAGE_CMD, cmdBean);
            localMinaServerController.send(cmdMessage);
        }
    }
}
