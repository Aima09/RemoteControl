package com.yf.remotecontrolclient.service.imp;


import com.yf.remotecontrolclient.domain.OpenSettings;
import com.yf.remotecontrolclient.minaclient.tcp.MinaMessageManager;
import com.yf.remotecontrolclient.service.SettingsBusinessService;
import com.yf.remotecontrolclient.util.JsonAssistant;

public class SettingsBusinessServiceImpl implements SettingsBusinessService {
    private JsonAssistant jsonAssistant;
    public static final String CMD = "cmd";

    public SettingsBusinessServiceImpl() {
        super();
        this.jsonAssistant = new JsonAssistant();
    }

    @Override
    public void senBsOpenSettings(OpenSettings openSettings) {
        MinaMessageManager.getInstance()
                .sendControlCmd(jsonAssistant.createOpenSettings(openSettings));
    }

}
