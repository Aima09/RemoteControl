package server.yf.com.remotecontrolserver_as.service.impl;


import server.yf.com.remotecontrolserver_as.CommonConstant;
import server.yf.com.remotecontrolserver_as.localminaserver.LocalMinaCmdManager;
import server.yf.com.remotecontrolserver_as.remoteminaclient.ClientMinaCmdManager;
import server.yf.com.remotecontrolserver_as.service.ImageBusinessService;

public class ImageBusinessServiceImpl implements ImageBusinessService {
	@Override
	public void sendImage(String imageJson) {
		if(CommonConstant.LINE_TYPE==1){//局域网
			LocalMinaCmdManager.getInstance().sendControlCmd(imageJson);
		}else{//互联网
			ClientMinaCmdManager.getInstance()
					.sendControlCmd(imageJson,null);
		}
//		TCPIPServer.getInstans(MouseService.gateway, MouseService.equipment, TcpAnalyzerImpl.getInstans()).send(imageJson.getBytes());
	}
}
