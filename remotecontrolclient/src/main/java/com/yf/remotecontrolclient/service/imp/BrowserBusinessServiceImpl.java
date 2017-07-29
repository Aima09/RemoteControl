package com.yf.remotecontrolclient.service.imp;


import com.yf.remotecontrolclient.domain.OpenBrower;
import com.yf.remotecontrolclient.minaclient.tcp.MinaCmdManager;
import com.yf.remotecontrolclient.service.BrowserBussinessService;
import com.yf.remotecontrolclient.util.JsonAssistant;

public class BrowserBusinessServiceImpl implements BrowserBussinessService {
    private JsonAssistant jsonAssistant;
    public static final String CMD = "cmd";

    public BrowserBusinessServiceImpl() {
        super();
        this.jsonAssistant = new JsonAssistant();
    }

    @Override
    public void senBsOpenBrowser(OpenBrower openBrower) {
        MinaCmdManager.getInstance().sendControlCmd(jsonAssistant.createOpenBrowser(openBrower));
    }
}
