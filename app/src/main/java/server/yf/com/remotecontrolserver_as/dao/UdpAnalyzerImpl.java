package server.yf.com.remotecontrolserver_as.dao;

import android.content.Intent;

import server.yf.com.remotecontrolserver_as.App;
import server.yf.com.remotecontrolserver_as.domain.Gateway;
import server.yf.com.remotecontrolserver_as.domain.Palpitation;
import server.yf.com.remotecontrolserver_as.service.impl.MouseBusinessServiceImpl;
import server.yf.com.remotecontrolserver_as.ui.serice.MouseService;
import server.yf.com.remotecontrolserver_as.util.JsonAssistant;

//import android.util.Log;
public class UdpAnalyzerImpl implements AnalyzerInterface{
//	private static String TAG = "UdpAnalyzerImpl";
	private static UdpAnalyzerImpl analyzerImpl;
	public static UdpAnalyzerImpl getInstans(){
		if(analyzerImpl==null){
			analyzerImpl=new UdpAnalyzerImpl();
		}
	    return analyzerImpl;
	}
	private JsonAssistant jsonAssistant = new JsonAssistant();
	
	@Override
	public void analy(byte[] buffer,String receiverId) {
		String data = new String(buffer).trim();
//		Log.i(TAG, "data"+data);
		if(data.contains("key")){
			Gateway gateway=jsonAssistant.paseGateway(data);
			Intent intent = new Intent();
			intent.setAction(MouseBusinessServiceImpl.DAO_UDP_UDPSERVER);
			intent.putExtra(MouseBusinessServiceImpl.CMD, "gateway");
			intent.putExtra("gateway", gateway);
			// 发送自定义无序广播
			App.getAppContext().sendBroadcast(intent);
		}else if(data.contains("cmd")&&data.contains("palpitation")&&data.contains("ip")){
			MouseService.palpitationTime=System.currentTimeMillis();
			//接收到了心跳
			Palpitation palpitation=jsonAssistant.pasePalpitation(data);
			Intent intent = new Intent();
			intent.setAction(MouseBusinessServiceImpl.DAO_UDP_UDPSERVER);
			intent.putExtra(MouseBusinessServiceImpl.CMD, palpitation.getCmd());
			intent.putExtra("palpitation",palpitation);
			// 发送自定义无序广播
			App.getAppContext().sendBroadcast(intent);
		}else if(data.startsWith("wlinkwulian")){
			//自己发出去的数据 不要处理
			return ;
		}
	}
}
