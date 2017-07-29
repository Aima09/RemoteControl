package com.yf.remotecontrolserver.localminaserver;


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
    private LocalMinaServerController localMinaServerController;
    private static LocalMinaMassageManager instance;
    private List<MinaCmdCallBack> minaCmdCallBacks = new ArrayList<>();

    public interface MinaCmdCallBack {
        void minaCmdCallBack(Object message);
    }

    public static synchronized LocalMinaMassageManager getInstance() {
        if (instance == null) {
            synchronized (LocalMinaMassageManager.class) {
                if (instance == null)
                    instance = new LocalMinaMassageManager();
            }
        }
        return instance;
    }

    public void addMinaCmdCallBack(MinaCmdCallBack callBack) {
        if (!minaCmdCallBacks.contains(callBack)) {
            minaCmdCallBacks.add(callBack);
        }
    }

    public void removeMinaCmdCallBack(MinaCmdCallBack callBack) {
        if (minaCmdCallBacks.contains(callBack)) {
            minaCmdCallBacks.remove(callBack);
        }
    }

    public void exeMinaCmdCallBack(Object cmd) {
        for (MinaCmdCallBack callBack : minaCmdCallBacks) {
            callBack.minaCmdCallBack(cmd);
        }
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
        }
    }

    public void disposeIntercom(IntercomMessage intercomMessage) {
        IntercomBean intercomBean = intercomMessage.getIntercomBean();
        Log.d("LocalMinaMassageManager", "MessageQueue.getInstance " + Arrays.toString(intercomBean.getIntercomContent()));
        AudioData audioData = new AudioData(intercomBean.getIntercomContent());
        MessageQueue.getInstance(MessageQueue.DECODER_DATA_QUEUE).put(audioData);
    }

    public void sendControlCmd(String cmdContent) {
        if (null != localMinaServerController) {
            Log.i("LocalMinaMassageManager", "发送数据");
            CmdBean cmdBean = new CmdBean(CmdType.CMD_MUSIC, DeviceType.DEVICE_TYPE_PHONE, cmdContent);
            CmdMessage cmdMessage = new CmdMessage(MessageType.MESSAGE_CMD, cmdBean);
            localMinaServerController.send(cmdMessage);
        }
    }
}