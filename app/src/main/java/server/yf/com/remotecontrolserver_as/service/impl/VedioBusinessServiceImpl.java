package server.yf.com.remotecontrolserver_as.service.impl;


import server.yf.com.remotecontrolserver_as.CommonConstant;
import server.yf.com.remotecontrolserver_as.localminaserver.LocalMinaCmdManager;
import server.yf.com.remotecontrolserver_as.remoteminaclient.ClientMinaCmdManager;
import server.yf.com.remotecontrolserver_as.service.VedioBusinessService;

public class VedioBusinessServiceImpl implements VedioBusinessService {
	@Override
	public void sendVedio(String vedioJson) {
		if(CommonConstant.LINE_TYPE==1){//局域网
			LocalMinaCmdManager.getInstance().sendControlCmd(vedioJson);
		}else{//互联网
			ClientMinaCmdManager.getInstance()
					.sendControlCmd(vedioJson,null);
		}
//		TCPIPServer.getInstans(MouseService.gateway, MouseService.equipment, TcpAnalyzerImpl.getInstans()).send(vedioJson.getBytes());
	}
}
