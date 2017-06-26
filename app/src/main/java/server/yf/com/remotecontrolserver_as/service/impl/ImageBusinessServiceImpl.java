package server.yf.com.remotecontrolserver_as.service.impl;


import server.yf.com.remotecontrolserver_as.dao.TcpAnalyzerImpl;
import server.yf.com.remotecontrolserver_as.dao.tcpip.TCPIPServer;
import server.yf.com.remotecontrolserver_as.service.ImageBusinessService;
import server.yf.com.remotecontrolserver_as.ui.serice.MouseService;

public class ImageBusinessServiceImpl implements ImageBusinessService {
	@Override
	public void sendImage(String imageJson) {
		TCPIPServer.getInstans(MouseService.gateway, MouseService.equipment, TcpAnalyzerImpl.getInstans()).send(imageJson.getBytes());
	}
}
