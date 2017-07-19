package com.yf.remotecontrolserver.common;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.yf.remotecontrolserver.zyglq.FileBusinessService;
import com.yf.remotecontrolserver.image.ImageBusinessService;
import com.yf.remotecontrolserver.music.MusicBusinessService;
import com.yf.remotecontrolserver.video.VedioBusinessService;
import com.yf.remotecontrolserver.zyglq.ZyglqBusinessService;
import com.yf.remotecontrolserver.zyglq.FileBusinessServiceImpl;
import com.yf.remotecontrolserver.image.ImageBusinessServiceImpl;
import com.yf.remotecontrolserver.music.MusicBusinessServiceImpl;
import com.yf.remotecontrolserver.video.VedioBusinessServiceImpl;
import com.yf.remotecontrolserver.zyglq.ZyglqBusinessServiceImpl;

/**
 * Created by sujuntao on 2017/7/14.
 * 多种类型的广播接收器
 */
public class DataTypeReceiver extends BroadcastReceiver {
    private final String TAG="DataTypeReceiver";
    public final String DATA_TYPE_RECEIVER_KEY="com.yf.remotecontrolserver.ui.receiver.dataTypeReceiver";
    private final String DATA_TYPE_KEY="dataTypeKey";
    private final String DATA_KEY="dataKey";
    FileBusinessService fileBusinessService=new FileBusinessServiceImpl();
    ImageBusinessService imageBusinessService=new ImageBusinessServiceImpl();
    MusicBusinessService musicBusinessService=new MusicBusinessServiceImpl();
    VedioBusinessService vedioBusinessService=new VedioBusinessServiceImpl();
    ZyglqBusinessService zyglqBusinessService=new ZyglqBusinessServiceImpl();
    @Override
    public void onReceive(Context context, Intent intent) {
        String dataType=intent.getStringExtra(DATA_TYPE_KEY);
        String data=intent.getStringExtra(DATA_KEY);
        if (TextUtils.isEmpty(dataType)){
            Log.i(TAG,"dataType为空");
        }else{
            if("file".equals(dataType)){
                fileBusinessService.sendFile(data,intent.getStringExtra("receiverId"));
            }else if("image".equals(dataType)){
                imageBusinessService.sendImage(data,intent.getStringExtra("receiverId"));
            }else if("music".equals(dataType)){
                musicBusinessService.sendMusic(data,intent.getStringExtra("receiverId"));
            }else if("video".equals(dataType)){
                vedioBusinessService.sendVedio(data,intent.getStringExtra("receiverId"));
            }else if("zyglq".equals(dataType)){
                zyglqBusinessService.sendZyglq(data,intent.getStringExtra("receiverId"));
            }
        }
    }
}
