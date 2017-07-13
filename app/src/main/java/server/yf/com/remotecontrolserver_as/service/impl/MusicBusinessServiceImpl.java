package server.yf.com.remotecontrolserver_as.service.impl;


import server.yf.com.remotecontrolserver_as.CommonConstant;
import server.yf.com.remotecontrolserver_as.localminaserver.LocalMinaCmdManager;
import server.yf.com.remotecontrolserver_as.remoteminaclient.ClientMinaCmdManager;
import server.yf.com.remotecontrolserver_as.service.MusicBusinessService;

public class MusicBusinessServiceImpl implements MusicBusinessService {

    @Override
    public void sendMusic(String musicListJson, String receiverId) {
        if (CommonConstant.LINE_TYPE == 1) {//局域网
            LocalMinaCmdManager.getInstance().sendControlCmd(musicListJson);
        } else {//互联网
            ClientMinaCmdManager.getInstance()
                    .sendControlCmd(musicListJson, receiverId);
        }
//		TCPIPServer.getInstans(MouseService.gateway, MouseService.equipment, TcpAnalyzerImpl.getInstans()).send(musicListJson.getBytes());
    }
}
