package com.yf.remotecontrolserver.localminaserver;


import android.util.Log;

import com.yf.remotecontrolserver.dao.TcpAnalyzerImpl;
import com.yf.remotecontrolserver.localminaserver.library.common.CmdType;
import com.yf.remotecontrolserver.localminaserver.library.common.DeviceType;
import com.yf.remotecontrolserver.localminaserver.library.common.MessageType;
import com.yf.remotecontrolserver.localminaserver.library.message.CmdMessage;
import com.yf.remotecontrolserver.localminaserver.library.message.CmdMessage.CmdBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuhuai on 2017/6/23 .
 * ;
 */

public class LocalMinaCmdManager {
    private LocalMinaServerController localMinaServerController;
    private static LocalMinaCmdManager instance;
    private List<MinaCmdCallBack> minaCmdCallBacks = new ArrayList<>();

    public interface MinaCmdCallBack {
        void minaCmdCallBack(Object message);
    }

    public static synchronized LocalMinaCmdManager getInstance() {
        if (instance == null) {
            synchronized (LocalMinaCmdManager.class) {
                if (instance == null)
                    instance = new LocalMinaCmdManager();
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

    public void disposeCmd(CmdMessage cmdMessage){
        CmdBean cmdBean = cmdMessage.getCmdBean();
        String cmdType = cmdBean.getCmdType();
        switch (cmdType) {
            case CmdType.CMD_MUSIC:
                Log.d("MinaCmdManager", "接收到音乐命令：" + cmdBean.getCmdContent());
                TcpAnalyzerImpl.getInstans().analy(cmdBean.getCmdContent().getBytes(),null);
                break;
        }
    }

    public void sendControlCmd(String cmdContent) {
        if (null != localMinaServerController) {
            Log.i("LocalMinaCmdManager","发送数据");
            CmdBean cmdBean = new CmdBean( CmdType.CMD_MUSIC, DeviceType.DEVICE_TYPE_INVALID,cmdContent);
            CmdMessage cmdMessage = new CmdMessage(MessageType.MESSAGE_CMD, cmdBean);
            localMinaServerController.send(cmdMessage);
        }
    }
}
