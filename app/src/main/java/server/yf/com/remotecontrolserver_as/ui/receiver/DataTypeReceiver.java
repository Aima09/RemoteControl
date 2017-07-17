package server.yf.com.remotecontrolserver_as.ui.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import server.yf.com.remotecontrolserver_as.service.FileBusinessService;
import server.yf.com.remotecontrolserver_as.service.ImageBusinessService;
import server.yf.com.remotecontrolserver_as.service.MusicBusinessService;
import server.yf.com.remotecontrolserver_as.service.VedioBusinessService;
import server.yf.com.remotecontrolserver_as.service.ZyglqBusinessService;
import server.yf.com.remotecontrolserver_as.service.impl.FileBusinessServiceImpl;
import server.yf.com.remotecontrolserver_as.service.impl.ImageBusinessServiceImpl;
import server.yf.com.remotecontrolserver_as.service.impl.MusicBusinessServiceImpl;
import server.yf.com.remotecontrolserver_as.service.impl.VedioBusinessServiceImpl;
import server.yf.com.remotecontrolserver_as.service.impl.ZyglqBusinessServiceImpl;

/**
 * Created by sujuntao on 2017/7/14.
 * 多种类型的广播接收器
 */
public class DataTypeReceiver extends BroadcastReceiver {
    private final String TAG="DataTypeReceiver";
    public final String DATA_TYPE_RECEIVER_KEY="server.yf.com.remotecontrolserver_as.ui.receiver.dataTypeReceiver";
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
                fileBusinessService.sendFile(data);
            }else if("image".equals(dataType)){
                imageBusinessService.sendImage(data);
            }else if("music".equals(dataType)){
                musicBusinessService.sendMusic(data,intent.getStringExtra("receiverId"));
            }else if("video".equals(dataType)){
                vedioBusinessService.sendVedio(data);
            }else if("zyglq".equals(dataType)){
                zyglqBusinessService.sendZyglq(data);
            }
        }
    }
}
