package server.yf.com.remotecontrolserver_as.ui.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import server.yf.com.remotecontrolserver_as.service.ZyglqBusinessService;
import server.yf.com.remotecontrolserver_as.service.impl.ZyglqBusinessServiceImpl;

public class ZyglReceiver extends BroadcastReceiver {
    ZyglqBusinessService businessService=new ZyglqBusinessServiceImpl();
	public final String TAG="ZyglReceiver";
	public static final String ZYGLQ_KEY = "Zyglq_key";
	private final String VEDIORECEIVER="COM.YF.SERVER.UI.RECEIVER.VIDEORECEIVER";
	@Override
	public void onReceive(Context context, Intent intent) {
		String data=intent.getStringExtra(ZYGLQ_KEY);
		Log.i(TAG, "data="+data);
		businessService.sendZyglq(data);
	}
}