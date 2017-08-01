package com.yf.remotecontrolclient.service.imp;


import android.text.TextUtils;

import com.yf.remotecontrolclient.activity.service.MouseService;
import com.yf.remotecontrolclient.dao.UdpAnalyzerImpl;
import com.yf.remotecontrolclient.dao.udp.UDPServer;
import com.yf.remotecontrolclient.domain.Action;
import com.yf.remotecontrolclient.domain.Boot;
import com.yf.remotecontrolclient.domain.Gateway;
import com.yf.remotecontrolclient.domain.Palpitation;
import com.yf.remotecontrolclient.domain.Position;
import com.yf.remotecontrolclient.domain.Writer;
import com.yf.remotecontrolclient.minaclient.tcp.MinaMessageManager;
import com.yf.remotecontrolclient.service.MouseBusinessService;
import com.yf.remotecontrolclient.util.JsonAssistant;

public class MouseBusinessServiceImpl implements MouseBusinessService {
    //	private final String TAG="MouseBusinessServiceImpl";
    private JsonAssistant jsonAssistant;
    public static final String CMD = "cmd";
    private int i = 0;

    public MouseBusinessServiceImpl() {
        super();
        jsonAssistant = new JsonAssistant();
    }

    //找到设备
    @Override
    public void echoEquipment(Gateway gateway) {
        UDPServer.getInstans(MouseService.gateway, MouseService.equipment, UdpAnalyzerImpl.getInstans()).send(jsonAssistant.createMessage(gateway).getBytes());
    }

    //设备发boot  我们回应boot 连接
    @Override
    public void linkEquipment(Boot boot) {
        String bootjsonString = jsonAssistant.paseBoot(boot);
        if (!TextUtils.isEmpty(bootjsonString)) {
            MinaMessageManager.getInstance().sendControlCmd(bootjsonString);
        }
    }

    //发位置
    @Override
    public synchronized void sendPosition(Position position) {
        MinaMessageManager.getInstance()
                .sendControlCmd(jsonAssistant.createPosition(position));
    }

    @Override
    public void sendAction(Action action) {
        MinaMessageManager.getInstance()
                .sendControlCmd(jsonAssistant.createAction(action));
    }

    @Override
    public void sendPalpitation(Palpitation palpitation) {
        MinaMessageManager.getInstance()
                .sendControlCmd(jsonAssistant.createPalpitation(palpitation));
    }

    @Override
    public void sendWriter(Writer writer) {
        MinaMessageManager.getInstance()
                .sendControlCmd(jsonAssistant.createWriter(writer));
    }
}
