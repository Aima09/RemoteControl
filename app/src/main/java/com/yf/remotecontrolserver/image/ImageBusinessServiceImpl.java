package com.yf.remotecontrolserver.image;


import com.yf.remotecontrolserver.common.CommonConstant;
import com.yf.remotecontrolserver.localminaserver.LocalMinaCmdManager;
import com.yf.remotecontrolserver.remoteminaclient.ClientMinaCmdManager;

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
