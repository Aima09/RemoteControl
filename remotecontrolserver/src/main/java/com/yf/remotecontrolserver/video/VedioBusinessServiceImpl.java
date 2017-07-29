package com.yf.remotecontrolserver.video;


import com.yf.remotecontrolserver.common.CommonConstant;
import com.yf.remotecontrolserver.localminaserver.LocalMinaMassageManager;
import com.yf.remotecontrolserver.remoteminaclient.ClientMinaCmdManager;

public class VedioBusinessServiceImpl implements VedioBusinessService {
    @Override
    public void sendVedio(String vedioJson, String receiverId) {
        if (CommonConstant.LINE_TYPE == 1) {//局域网
            LocalMinaMassageManager.getInstance().sendControlCmd(vedioJson);
        } else {//互联网
            ClientMinaCmdManager.getInstance()
                    .sendControlCmd(vedioJson, receiverId);
        }
//		TCPIPServer.getInstans(MouseService.gateway, MouseService.equipment, TcpAnalyzerImpl.getInstans()).send(vedioJson.getBytes());
    }
}
