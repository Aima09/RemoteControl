package com.yf.remotecontrolserver.image;


import com.yf.remotecontrolserver.common.CommonConstant;
import com.yf.remotecontrolserver.localminaserver.LocalMinaMassageManager;
import com.yf.remotecontrolserver.remoteminaclient.ClientMinaCmdManager;

public class ImageBusinessServiceImpl implements ImageBusinessService {
    @Override
    public void sendImage(String imageJson, String receiverId) {
        if (CommonConstant.LINE_TYPE == 1) {//局域网
            LocalMinaMassageManager.getInstance().sendControlCmd(imageJson);
        } else {//互联网
            ClientMinaCmdManager.getInstance()
                    .sendControlCmd(imageJson, receiverId);
        }
//		TCPIPServer.getInstans(MouseService.gateway, MouseService.equipment, TcpAnalyzerImpl.getInstans()).send(imageJson.getBytes());
    }
}
