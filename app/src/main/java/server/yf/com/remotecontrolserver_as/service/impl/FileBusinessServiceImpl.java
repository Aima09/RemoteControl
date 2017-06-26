package server.yf.com.remotecontrolserver_as.service.impl;


import server.yf.com.remotecontrolserver_as.dao.TcpAnalyzerImpl;
import server.yf.com.remotecontrolserver_as.dao.tcpip.TCPIPServer;
import server.yf.com.remotecontrolserver_as.service.FileBusinessService;
import server.yf.com.remotecontrolserver_as.ui.serice.MouseService;

public class FileBusinessServiceImpl implements FileBusinessService {
	@Override
	public void sendFile(String fileJson) {
		TCPIPServer.getInstans(MouseService.gateway, MouseService.equipment, TcpAnalyzerImpl.getInstans()).send(fileJson.getBytes());
	}
}
