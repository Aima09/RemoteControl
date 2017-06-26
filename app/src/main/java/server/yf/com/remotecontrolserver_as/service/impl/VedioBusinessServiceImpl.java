package server.yf.com.remotecontrolserver_as.service.impl;


import server.yf.com.remotecontrolserver_as.dao.TcpAnalyzerImpl;
import server.yf.com.remotecontrolserver_as.dao.tcpip.TCPIPServer;
import server.yf.com.remotecontrolserver_as.service.VedioBusinessService;
import server.yf.com.remotecontrolserver_as.ui.serice.MouseService;

public class VedioBusinessServiceImpl implements VedioBusinessService {
	@Override
	public void sendVedio(String vedioJson) {
		TCPIPServer.getInstans(MouseService.gateway, MouseService.equipment, TcpAnalyzerImpl.getInstans()).send(vedioJson.getBytes());
	}
}
