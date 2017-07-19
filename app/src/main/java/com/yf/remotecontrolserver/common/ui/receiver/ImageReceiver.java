package com.yf.remotecontrolserver.common.ui.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yf.remotecontrolserver.image.ImageBusinessService;
import com.yf.remotecontrolserver.image.ImageBusinessServiceImpl;

public class ImageReceiver extends BroadcastReceiver {
	private final String TAG="ImageReceiver";
    ImageBusinessService imageBusinessService=new ImageBusinessServiceImpl();
//	public final String TAG="ImageReceiver";
    //play图片的数据key
  	public static final String IMAGE_KEY="image_key";
	@Override
	public void onReceive(Context context, Intent intent) {
		String data=intent.getStringExtra(IMAGE_KEY);
		//imageBusinessService.sendImage(data);
	}
}