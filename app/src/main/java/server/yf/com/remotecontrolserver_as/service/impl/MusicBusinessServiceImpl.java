package server.yf.com.remotecontrolserver_as.service.impl;


import server.yf.com.remotecontrolserver_as.CommonConstant;
import server.yf.com.remotecontrolserver_as.LanMina.LanMinaCmdManager;
import server.yf.com.remotecontrolserver_as.dao.TcpAnalyzerImpl;
import server.yf.com.remotecontrolserver_as.dao.tcpip.TCPIPServer;
import server.yf.com.remotecontrolserver_as.minamachines.MinaCmdManager;
import server.yf.com.remotecontrolserver_as.service.MusicBusinessService;
import server.yf.com.remotecontrolserver_as.ui.serice.MouseService;

public class MusicBusinessServiceImpl implements MusicBusinessService {

	@Override
	public void sendMusic(String musicListJson) {
		if(CommonConstant.LINE_TYPE==1){//局域网
			LanMinaCmdManager.getInstance().sendControlCmd(musicListJson);
		}else{//互联网
			MinaCmdManager.getInstance()
					.sendControlCmd(musicListJson);
		}
//		TCPIPServer.getInstans(MouseService.gateway, MouseService.equipment, TcpAnalyzerImpl.getInstans()).send(musicListJson.getBytes());
	}
}
