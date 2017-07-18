package com.yf.remotecontrolserver.util;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class IpUtil {
	/**
	 * 获取本地ip地址
	 * 
	 * @return
	 */
	public static String getLocalIpAddress(Context context) {

		String hostIp = null;
		try {
			Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
			InetAddress ia = null;
			while (nis.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) nis.nextElement();
				Enumeration<InetAddress> ias = ni.getInetAddresses();
				while (ias.hasMoreElements()) {
					ia = ias.nextElement();
					if (ia instanceof Inet6Address) {
						continue;// skip ipv6
					}
					String ip = ia.getHostAddress();
					if (!"127.0.0.1".equals(ip)) {
						hostIp = ia.getHostAddress();
						break;
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		if (hostIp == null) {
			hostIp = getLocalWifiStringIpAddress(context);
		}
		return hostIp;
	}

	@SuppressLint("DefaultLocale")
	public static String getLocalWifiStringIpAddress(Context context) {
		// 获取wifi服务
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		// 判断wifi是否开启
		if (!wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);
		}
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		// 返回整型地址转换成“*.*.*.*”地址
		return String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff),
				(ipAddress >> 24 & 0xff));
	}
	
	/**
	 * 获取网关所有的ip地址
	 */
	public static InetAddress getBroadcastAddress(Context context) throws UnknownHostException {
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		DhcpInfo dhcp = wifi.getDhcpInfo();
		if (dhcp == null) {
			return InetAddress.getByName("255.255.255.255");
		}
		int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
		byte[] quads = new byte[4];
		for (int k = 0; k < 4; k++)
			quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
//		Log.i("IpUtilInetAddress=",InetAddress.getByAddress(quads).getHostAddress());
		return InetAddress.getByAddress(quads);
	}
}
