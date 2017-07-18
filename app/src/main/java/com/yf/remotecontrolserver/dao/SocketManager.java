package com.yf.remotecontrolserver.dao;


import com.yf.remotecontrolserver.dao.udp.UDPServer;
import com.yf.remotecontrolserver.common.ui.serice.MouseService;


public class SocketManager {
	public static final String TAG = "SocketManager";
	private static SocketManager socketManager;

	public static SocketManager getSocketManager() {
		if (socketManager == null) {
			socketManager = new SocketManager();
		}
		return socketManager;
	}

	public void startUdp() {
		UDPServer.getInstans(MouseService.gateway, MouseService.equipment,
				UdpAnalyzerImpl.getInstans()).start();
	}
}
