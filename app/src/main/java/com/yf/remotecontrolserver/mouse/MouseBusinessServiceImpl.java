package com.yf.remotecontrolserver.mouse;


import android.content.Intent;
import android.text.TextUtils;

import com.yf.remotecontrolserver.common.App;
import com.yf.remotecontrolserver.common.CommonConstant;
import com.yf.remotecontrolserver.domain.Boot;
import com.yf.remotecontrolserver.domain.Equipment;
import com.yf.remotecontrolserver.domain.Palpitation;
import com.yf.remotecontrolserver.localminaserver.LocalMinaCmdManager;
import com.yf.remotecontrolserver.remoteminaclient.ClientMinaCmdManager;
import com.yf.remotecontrolserver.util.JsonAssistant;

public class MouseBusinessServiceImpl implements MouseBusinessService {
//	public final String TAG="MouseBusinessServiceImpl";
	private JsonAssistant jsonAssistant;
	public static final String CMD="cmd";
	
	public MouseBusinessServiceImpl() {
		jsonAssistant=new JsonAssistant();
	}

	@Override
	public void echoGateway(Equipment equipment) {
		try {
//			Log.i(TAG, "发信息去找客户端1");
			String data="wlinkwulian"+equipment.getDevid();
//			Log.i(TAG, "data="+data);
			if(CommonConstant.LINE_TYPE==1){//局域网
				LocalMinaCmdManager.getInstance().sendControlCmd("wlinkwulian"+equipment.getDevid());
			}else{//互联网
				ClientMinaCmdManager.getInstance()
						.sendControlCmd("wlinkwulian"+equipment.getDevid(),null);
			}
//			UDPServer.getInstans(MouseService.gateway, MouseService.equipment, UdpAnalyzerImpl.getInstans()).send(("wlinkwulian"+equipment.getDevid()).getBytes());
//			Log.i(TAG, "发信息去找客户端2");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//设备发boot  我们回应boot 连接
	@Override
	public void linkGateway(Boot boot){
		String bootjsonString=jsonAssistant.paseBoot(boot);
		if(!TextUtils.isEmpty(bootjsonString)){
			if(CommonConstant.LINE_TYPE==1){//局域网
				LocalMinaCmdManager.getInstance().sendControlCmd(bootjsonString);
			}else{//互联网
				ClientMinaCmdManager.getInstance()
						.sendControlCmd(bootjsonString,null);
			}
//			TCPIPServer.getInstans(MouseService.gateway, MouseService.equipment, TcpAnalyzerImpl.getInstans()).send((bootjsonString+"#").getBytes());
		}
	}


	/**
	 * 发广播给mouseKey
	 */
	@Override
	public void move(String movedata) {
		Intent intent=new Intent();
		intent.setAction(SERVICE_MOUSE);
		intent.putExtra(MOUSE_key, movedata);
//		Log.i(TAG, SERVICE_MOUSE);
//		Log.i(TAG,"MOUSE_key"+SERVICE_MOUSE+":"+movedata);
		App.getAppContext().sendBroadcast(intent);
	}
 
	@Override
	public void mode(String modedata) {
		Intent intent=new Intent();
		intent.setAction(MOUSE_MODE);
		intent.putExtra(MODE_key, Integer.valueOf(modedata));
		App.getAppContext().sendBroadcast(intent);
	}

	@Override
	public void key(String keydata) {
		Intent intent=new Intent();
		intent.setAction(SERVICE_KEY);
		intent.putExtra(KEY_key, Integer.valueOf(keydata));
		App.getAppContext().sendBroadcast(intent);
	}

	@Override
	public void home() {
//		Log.i(TAG, "home");
		Intent intent= new Intent(Intent.ACTION_MAIN);  

		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //如果是服务里调用，必须加入new task标识    

		intent.addCategory(Intent.CATEGORY_HOME);

		App.getAppContext().startActivity(intent); 
	}
	
	@Override
	public void sendPalpitation(Palpitation palpitation) {
		if(CommonConstant.LINE_TYPE==1){//局域网
			LocalMinaCmdManager.getInstance().sendControlCmd(jsonAssistant.createPalpitation(palpitation));
		}else{//互联网
			ClientMinaCmdManager.getInstance()
					.sendControlCmd(jsonAssistant.createPalpitation(palpitation),null);
		}
//		UDPServer.getInstans(MouseService.gateway, MouseService.equipment, TcpAnalyzerImpl.getInstans()).send((jsonAssistant.createPalpitation(palpitation)+"#").getBytes());
	}
}
