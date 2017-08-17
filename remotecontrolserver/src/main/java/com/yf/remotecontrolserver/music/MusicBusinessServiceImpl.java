package com.yf.remotecontrolserver.music;


import android.util.Log;

import com.yf.remotecontrolserver.common.CommonConstant;
import com.yf.remotecontrolserver.localminaserver.LocalMinaMassageManager;
import com.yf.remotecontrolserver.remoteminaclient.ClientMinaCmdManager;

public class MusicBusinessServiceImpl implements MusicBusinessService {

    @Override
    public void sendMusic(String musicListJson, String receiverId) {
        Log.i("MusicBusi","sdaf1receiverId = "+receiverId);
        if (CommonConstant.LINE_TYPE == 1) {//局域网
            Log.i("MusicBusi","sdafreceiverId2"+receiverId);
            LocalMinaMassageManager.getInstance().sendControlCmd(musicListJson);
        } else {//互联网
            ClientMinaCmdManager.getInstance()
                    .sendControlCmd(musicListJson, receiverId);
        }
//		TCPIPServer.getInstans(MouseService.gateway, MouseService.equipment, TcpAnalyzerImpl.getInstans()).send(musicListJson.getBytes());
    }
}
