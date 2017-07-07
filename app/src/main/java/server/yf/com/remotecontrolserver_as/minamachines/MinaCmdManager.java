package server.yf.com.remotecontrolserver_as.minamachines;


import android.util.Log;

import com.yf.minalibrary.common.CmdType;
import com.yf.minalibrary.common.DeviceType;
import com.yf.minalibrary.common.MessageType;
import com.yf.minalibrary.message.CmdMessage;
import com.yf.minalibrary.message.CmdMessage.CmdBean;

import java.util.ArrayList;
import java.util.List;

import server.yf.com.remotecontrolserver_as.dao.TcpAnalyzerImpl;
import server.yf.com.remotecontrolserver_as.dao.tcpip.TCPIPServer;

/**
 * Created by wuhuai on 2017/6/23 .
 * ;
 */

public class MinaCmdManager {

    private static MinaCmdManager instance;
    private List<MinaCmdCallBack> minaCmdCallBacks = new ArrayList<>();
    private MinaServerController minaServerController;
    private DevicesManager devicesManager;

    private IdListener idListener;
    public interface MinaCmdCallBack {
        void minaCmdCallBack(Object message);
    }

    public static synchronized MinaCmdManager getInstance() {
        if (instance == null) {
            synchronized (MinaCmdManager.class) {
                if (instance == null)
                    instance = new MinaCmdManager();
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

    public void disposeCmd(CmdMessage cmdMessage){
        CmdBean cmdBean = cmdMessage.getCmdBean();
        String cmdType = cmdBean.getCmdType();
        Log.d("MinaCmdManager", "CMD_BUSINESS"+cmdBean.getCmdContent());
        switch (cmdType) {
            case CmdType.CMD_REGISTER:
                String uuid = cmdBean.getCmdContent();
                ServerDataDisposeCenter.setLocalSenderId(uuid);
                Log.d("MinaCmdManager", uuid);
                Log.d("IoClientHandler", uuid);
                if (this.idListener!=null) {
                    idListener.flushId(uuid);
                }
                break;
            case CmdType.CMD_LOGIN:
                String loginResult = cmdBean.getCmdContent();
                Log.d("MinaCmdManager", loginResult);
                if (this.idListener!=null) {
                    idListener.flushId(ServerDataDisposeCenter.getLocalSenderId());
                }
                break;
            case CmdType.CMD_MUSIC:
                TcpAnalyzerImpl.getInstans().analy(cmdBean.getCmdContent().getBytes(),cmdBean.getSenderId());
                Log.d("MinaCmdManager", "CMD_MUSIC"+cmdBean.getCmdContent());
                Log.d("MinaCmdManager", "rid"+cmdBean.getReceiverId());
                sendControlCmd("音乐命令：+music",cmdBean.getReceiverId());
                break;
        }
    }

    public void sendControlCmd(String cmdContent,String receiverId) {
        if (null != minaServerController) {
            CmdBean cmdBean = new CmdBean(ServerDataDisposeCenter.getLocalSenderId(),
                    receiverId, CmdType.CMD_MUSIC, DeviceType.DEVICE_TYPE_PHONE,cmdContent);

            CmdMessage cmdMessage = new CmdMessage(MessageType.MESSAGE_CMD, cmdBean);
            minaServerController.send(cmdMessage);
        }
    }


    public void setIdListener(IdListener idListener) {
        this.idListener = idListener;
    }

    public interface IdListener{
       void flushId(String id);
    }

}
