package server.yf.com.remotecontrolserver_as.service.impl;


import server.yf.com.remotecontrolserver_as.dao.TcpAnalyzerImpl;
import server.yf.com.remotecontrolserver_as.dao.tcpip.TCPIPServer;
import server.yf.com.remotecontrolserver_as.service.ZyglqBusinessService;
import server.yf.com.remotecontrolserver_as.ui.serice.MouseService;
import server.yf.com.remotecontrolserver_as.util.JsonAssistant;

public class ZyglqBusinessServiceImpl implements ZyglqBusinessService {
	private JsonAssistant jsonAssistant;
	public static final String CMD="cmd";
	@Override
	public void sendZyglq(String zyglqJson) {
		TCPIPServer.getInstans(MouseService.gateway, MouseService.equipment, TcpAnalyzerImpl.getInstans()).send(zyglqJson.getBytes());
	}
}
