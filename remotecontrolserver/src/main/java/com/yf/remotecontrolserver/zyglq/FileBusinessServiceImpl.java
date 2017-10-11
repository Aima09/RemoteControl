package com.yf.remotecontrolserver.zyglq;


import com.yf.remotecontrolserver.common.CommonConstant;
import com.yf.remotecontrolserver.localminaserver.LocalMinaMassageManager;
import com.yf.remotecontrolserver.remoteminaclient.ClientMinaCmdManager;

public class FileBusinessServiceImpl implements FileBusinessService {
    @Override
    public void sendFile(String fileJson, String receiverId) {
        if (CommonConstant.LINE_TYPE == 1) {//局域网
            LocalMinaMassageManager.getInstance().sendControlCmd(fileJson);
        } else {//互联网
            ClientMinaCmdManager.getInstance()
                    .sendControlCmd(fileJson, receiverId);
        }
    }
}
