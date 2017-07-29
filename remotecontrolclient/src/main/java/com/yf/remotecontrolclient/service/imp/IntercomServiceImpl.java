package com.yf.remotecontrolclient.service.imp;

import com.yf.remotecontrolclient.minaclient.tcp.MinaCmdManager;
import com.yf.remotecontrolclient.service.IntercomService;
import com.yf.remotecontrolclient.util.JsonAssistant;

/**
 * Created by xuie on 17-7-28.
 * 对讲数据发送接口
 */
public class IntercomServiceImpl implements IntercomService {
    private JsonAssistant jsonAssistant;
    private static IntercomServiceImpl instance;
    public static final String CMD = "cmd";

    private IntercomServiceImpl() {
        this.jsonAssistant = new JsonAssistant();
    }

    public static IntercomServiceImpl get() {
        if (instance == null) {
            synchronized (IntercomServiceImpl.class) {
                instance = new IntercomServiceImpl();
            }
        }
        return instance;
    }

    @Override
    public void send(byte[] content) {
        if (content.length > 0){
            MinaCmdManager.getInstance().sendIntercomContent(content);
        }
    }
}
