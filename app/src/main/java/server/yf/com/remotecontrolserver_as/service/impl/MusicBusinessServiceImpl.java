package server.yf.com.remotecontrolserver_as.service.impl;


import server.yf.com.remotecontrolserver_as.dao.TcpAnalyzerImpl;
import server.yf.com.remotecontrolserver_as.dao.tcpip.TCPIPServer;
import server.yf.com.remotecontrolserver_as.service.MusicBusinessService;
import server.yf.com.remotecontrolserver_as.ui.serice.MouseService;

public class MusicBusinessServiceImpl implements MusicBusinessService {

	@Override
	public void sendMusic(String musicListJson) {
		TCPIPServer.getInstans(MouseService.gateway, MouseService.equipment, TcpAnalyzerImpl.getInstans()).send(musicListJson.getBytes());
	}
}
