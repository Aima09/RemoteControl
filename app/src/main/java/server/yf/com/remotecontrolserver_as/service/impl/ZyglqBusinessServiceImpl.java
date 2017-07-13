package server.yf.com.remotecontrolserver_as.service.impl;


import server.yf.com.remotecontrolserver_as.CommonConstant;
import server.yf.com.remotecontrolserver_as.localminaserver.LocalMinaCmdManager;
import server.yf.com.remotecontrolserver_as.remoteminaclient.ClientMinaCmdManager;
import server.yf.com.remotecontrolserver_as.service.ZyglqBusinessService;
import server.yf.com.remotecontrolserver_as.util.JsonAssistant;

public class ZyglqBusinessServiceImpl implements ZyglqBusinessService {
	private JsonAssistant jsonAssistant;
	public static final String CMD="cmd";
	@Override
	public void sendZyglq(String zyglqJson) {
		if(CommonConstant.LINE_TYPE==1){//局域网
			LocalMinaCmdManager.getInstance().sendControlCmd(zyglqJson);
		}else{//互联网
			ClientMinaCmdManager.getInstance()
					.sendControlCmd(zyglqJson,null);
		}
//		TCPIPServer.getInstans(MouseService.gateway, MouseService.equipment, TcpAnalyzerImpl.getInstans()).send(zyglqJson.getBytes());
	}
}
