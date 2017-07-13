package server.yf.com.remotecontrolserver_as.service.impl;


import server.yf.com.remotecontrolserver_as.CommonConstant;
import server.yf.com.remotecontrolserver_as.localminaserver.LocalMinaCmdManager;
import server.yf.com.remotecontrolserver_as.remoteminaclient.ClientMinaCmdManager;
import server.yf.com.remotecontrolserver_as.service.FileBusinessService;

public class FileBusinessServiceImpl implements FileBusinessService {
	@Override
	public void sendFile(String fileJson) {
		if(CommonConstant.LINE_TYPE==1){//局域网
			LocalMinaCmdManager.getInstance().sendControlCmd(fileJson);
		}else{//互联网
			ClientMinaCmdManager.getInstance()
					.sendControlCmd(fileJson,null);
		}
//		TCPIPServer.getInstans(MouseService.gateway, MouseService.equipment, TcpAnalyzerImpl.getInstans()).send(fileJson.getBytes());
	}
}
