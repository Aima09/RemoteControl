package com.yf.remotecontrolserver.zyglq;


import com.yf.remotecontrolserver.common.CommonConstant;
import com.yf.remotecontrolserver.localminaserver.LocalMinaMassageManager;
import com.yf.remotecontrolserver.remoteminaclient.ClientMinaCmdManager;
import com.yf.remotecontrolserver.util.JsonAssistant;

public class ZyglqBusinessServiceImpl implements ZyglqBusinessService {
    private JsonAssistant jsonAssistant;
    public static final String CMD = "cmd";

    @Override
    public void sendZyglq(String zyglqJson, String receiverId) {
        if (CommonConstant.LINE_TYPE == 1) {//局域网
            LocalMinaMassageManager.getInstance().sendControlCmd(zyglqJson);
        } else {//互联网
            ClientMinaCmdManager.getInstance()
                    .sendControlCmd(zyglqJson, receiverId);
        }
    }
}
