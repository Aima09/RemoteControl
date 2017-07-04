package server.yf.com.remotecontrolserver_as.service.impl;


import server.yf.com.remotecontrolserver_as.CommonConstant;
import server.yf.com.remotecontrolserver_as.LanMina.LanMinaCmdManager;
import server.yf.com.remotecontrolserver_as.dao.TcpAnalyzerImpl;
import server.yf.com.remotecontrolserver_as.dao.tcpip.TCPIPServer;
import server.yf.com.remotecontrolserver_as.minamachines.MinaCmdManager;
import server.yf.com.remotecontrolserver_as.service.ImageBusinessService;
import server.yf.com.remotecontrolserver_as.ui.serice.MouseService;

public class ImageBusinessServiceImpl implements ImageBusinessService {
	@Override
	public void sendImage(String imageJson) {
		if(CommonConstant.LINE_TYPE==1){//局域网
			LanMinaCmdManager.getInstance().sendControlCmd(imageJson);
		}else{//互联网
			MinaCmdManager.getInstance()
					.sendControlCmd(imageJson);
		}
//		TCPIPServer.getInstans(MouseService.gateway, MouseService.equipment, TcpAnalyzerImpl.getInstans()).send(imageJson.getBytes());
	}
}
