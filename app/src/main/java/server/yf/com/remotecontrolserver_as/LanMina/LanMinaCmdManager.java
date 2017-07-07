package server.yf.com.remotecontrolserver_as.LanMina;


import android.util.Log;

import server.yf.com.remotecontrolserver_as.LanMina.library.common.CmdType;
import server.yf.com.remotecontrolserver_as.LanMina.library.common.DeviceType;
import server.yf.com.remotecontrolserver_as.LanMina.library.common.MessageType;
import server.yf.com.remotecontrolserver_as.LanMina.library.message.CmdMessage;
import server.yf.com.remotecontrolserver_as.LanMina.library.message.CmdMessage.CmdBean;

import java.util.ArrayList;
import java.util.List;

import server.yf.com.remotecontrolserver_as.ui.serice.MouseService;

/**
 * Created by wuhuai on 2017/6/23 .
 * ;
 */

public class LanMinaCmdManager {
    private MinaServerController minaServerController;
    private static LanMinaCmdManager instance;
    private List<MinaCmdCallBack> minaCmdCallBacks = new ArrayList<>();

    public interface MinaCmdCallBack {
        void minaCmdCallBack(Object message);
    }

    public static synchronized LanMinaCmdManager getInstance() {
        if (instance == null) {
            synchronized (LanMinaCmdManager.class) {
                if (instance == null)
                    instance = new LanMinaCmdManager();
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

    public void setMinaServerController(MinaServerController minaServerController) {
        this.minaServerController = minaServerController;
    }

    public void sendControlCmd(String cmdContent) {
        if (null != minaServerController) {
            Log.i("LanMinaCmdManager","发送数据");
            CmdBean cmdBean = new CmdBean(MouseService.equipment.getIp(),
                    (String)IoServerHandler.currenSession.getAttribute("KEY_SESSION_CLIENT_IP"), CmdType.CMD_MUSIC, DeviceType.DEVICE_TYPE_INVALID,cmdContent);
            CmdMessage cmdMessage = new CmdMessage(MessageType.MESSAGE_CMD, cmdBean);
            minaServerController.send(cmdMessage);
        }
    }
}
