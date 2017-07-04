package server.yf.com.remotecontrolserver_as.ui.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import server.yf.com.remotecontrolserver_as.service.impl.MusicBusinessServiceImpl;

public class MusicReceiver extends BroadcastReceiver {
	MusicBusinessServiceImpl businessServiceImpl=new MusicBusinessServiceImpl();
//	public final String TAG="MusicReceiver";
	private String MUSIC_KEY="music_key";
	private final String MUSICRECEIVER="COM.YF.SERVER.UI.RECEIVER.MUSICRECEIVER";
	@Override
	public void onReceive(Context context, Intent intent) {
		String data=intent.getStringExtra(MUSIC_KEY);
		businessServiceImpl.sendMusic(data);
	}
}
