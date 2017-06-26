package server.yf.com.remotecontrolserver_as.util;


import server.yf.com.remotecontrolserver_as.domain.Gateway;

public class GatewayFactory {
	public static Gateway getGateway(){
		Gateway gateway=new Gateway();
		gateway.setGwTcpPort(7081);
		gateway.setGwUdpPort(7320);
		return gateway;
	}
}
