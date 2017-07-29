package com.yf.remotecontrolclient.util;


import com.yf.remotecontrolclient.App;
import com.yf.remotecontrolclient.domain.Gateway;

public class GatewayFactory {
    public static Gateway getGateway() {
        Gateway gateway = new Gateway();
        gateway.setKey("wlinkwulian");
        gateway.setGwID("wl0000001");
        gateway.setGwIp(IpUtil.getLocalIpAddress(App.getAppContext()));
        gateway.setGwPort(7081);
        gateway.setGwTcpPort(7081);
        gateway.setGwUdpPort(7320);

        return gateway;
    }
}
