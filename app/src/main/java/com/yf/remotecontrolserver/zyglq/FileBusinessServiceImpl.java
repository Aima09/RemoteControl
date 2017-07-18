package com.yf.remotecontrolserver.zyglq;


import com.yf.remotecontrolserver.common.CommonConstant;
import com.yf.remotecontrolserver.localminaserver.LocalMinaCmdManager;
import com.yf.remotecontrolserver.remoteminaclient.ClientMinaCmdManager;

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
