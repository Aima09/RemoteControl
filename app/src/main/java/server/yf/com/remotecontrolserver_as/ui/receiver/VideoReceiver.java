package server.yf.com.remotecontrolserver_as.ui.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import server.yf.com.remotecontrolserver_as.service.VedioBusinessService;
import server.yf.com.remotecontrolserver_as.service.impl.VedioBusinessServiceImpl;

public class VideoReceiver extends BroadcastReceiver {
    VedioBusinessService businessService=new VedioBusinessServiceImpl();
//	public final String TAG="VideoReceiver";
	private String VEDIO_KEY="VIDEO_KEY";
	private final String VEDIORECEIVER="COM.YF.SERVER.UI.RECEIVER.VIDEORECEIVER";
	@Override
	public void onReceive(Context context, Intent intent) {
		String data=intent.getStringExtra(VEDIO_KEY);
		businessService.sendVedio(data);
	}
}
