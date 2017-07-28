package com.yuanfang.intercom.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPUtil {
    public static String formatIpAddress(int ipAdress) {

        return (ipAdress & 0xFF) + "." + ((ipAdress >> 8) & 0xFF) + "." + ((ipAdress >> 16) & 0xFF) + "."
                + (ipAdress >> 24 & 0xFF);
    }

    //获取本地IP函数
    public static String getLocalIPAddress() {
//        //获取wifi服务
//        WifiManager wifiManager = (WifiManager) App.getInstance().getApplicationContext()
//                .getSystemService(Context.WIFI_SERVICE);
//        //判断wifi是否开启
//        if (!wifiManager.isWifiEnabled()) {
//            wifiManager.setWifiEnabled(true);
//        }
//        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//        int ipAddress = wifiInfo.getIpAddress();
//        return formatIpAddress(ipAddress);
        return null;
    }

    // default ip address?
    private static String intercomAddress = "192.168.1.100";

    public static synchronized InetAddress getIntercomAddress() {
        try {
            return InetAddress.getByName(IPUtil.intercomAddress);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static synchronized void setIntercomAddress(String intercomAddress) {
        IPUtil.intercomAddress = intercomAddress;
    }
}
