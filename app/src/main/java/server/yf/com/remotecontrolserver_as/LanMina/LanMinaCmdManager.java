package server.yf.com.remotecontrolserver_as.LanMina;


import android.util.Log;

import com.yf.minalibrary.common.CmdType;
import com.yf.minalibrary.common.DeviceType;
import com.yf.minalibrary.common.MessageType;
import com.yf.minalibrary.message.CmdMessage;
import com.yf.minalibrary.message.CmdMessage.CmdBean;

import java.util.ArrayList;
import java.util.List;

import server.yf.com.remotecontrolserver_as.minamachines.ServerDataDisposeCenter;

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
            CmdBean cmdBean = new CmdBean(ServerDataDisposeCenter.getLocalSenderId(),
                    ServerDataDisposeCenter.getRemoteReceiverId(), CmdType.CMD_MUSIC, DeviceType.DEVICE_TYPE_PHONE,cmdContent);
            CmdMessage cmdMessage = new CmdMessage(MessageType.MESSAGE_CMD, cmdBean);
            minaServerController.send(cmdMessage);
        }
    }

}
