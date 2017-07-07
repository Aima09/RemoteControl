package server.yf.com.remotecontrolserver_as.service.impl;


import server.yf.com.remotecontrolserver_as.CommonConstant;
import server.yf.com.remotecontrolserver_as.LanMina.LanMinaCmdManager;
import server.yf.com.remotecontrolserver_as.minamachines.MinaCmdManager;
import server.yf.com.remotecontrolserver_as.service.FileBusinessService;

public class FileBusinessServiceImpl implements FileBusinessService {
	@Override
	public void sendFile(String fileJson) {
		if(CommonConstant.LINE_TYPE==1){//局域网
			LanMinaCmdManager.getInstance().sendControlCmd(fileJson);
		}else{//互联网
			MinaCmdManager.getInstance()
					.sendControlCmd(fileJson,null);
		}
//		TCPIPServer.getInstans(MouseService.gateway, MouseService.equipment, TcpAnalyzerImpl.getInstans()).send(fileJson.getBytes());
	}
}
