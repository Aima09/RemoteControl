package com.yf.remotecontrolserver.util;


import com.yf.remotecontrolserver.domain.Gateway;

public class GatewayFactory {
	public static Gateway getGateway(){
		Gateway gateway=new Gateway();
		gateway.setGwTcpPort(7081);
		gateway.setGwUdpPort(7320);
		return gateway;
	}
}
