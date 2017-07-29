package com.yf.remotecontrolclient.dao;


import android.content.Intent;
import android.util.Log;

import com.yf.remotecontrolclient.App;
import com.yf.remotecontrolclient.activity.FileActivity;
import com.yf.remotecontrolclient.activity.FileShowListActivity;
import com.yf.remotecontrolclient.activity.MediaImageActivity;
import com.yf.remotecontrolclient.activity.MediaImageFolderActivity;
import com.yf.remotecontrolclient.activity.MediaMusicActivity;
import com.yf.remotecontrolclient.activity.MediaVideoActivity;
import com.yf.remotecontrolclient.service.imp.FileBusinessServiceImpl;
import com.yf.remotecontrolclient.service.imp.ImageBusinessServiceImpl;
import com.yf.remotecontrolclient.service.imp.MouseBusinessServiceImpl;
import com.yf.remotecontrolclient.service.imp.MusicBusinessServiceImpl;
import com.yf.remotecontrolclient.service.imp.VideoBusinessServiceImpl;
import com.yf.remotecontrolclient.util.JsonAssistant;

public class TcpAnalyzerImpl implements AnalyzerInterface {
    private static String TAG = "TcpAnalyzerImpl";
    private static TcpAnalyzerImpl analyzerImpl;

    public static TcpAnalyzerImpl getInstans() {
        if (analyzerImpl == null) {
            analyzerImpl = new TcpAnalyzerImpl();
        }
        return analyzerImpl;
    }

    private JsonAssistant jsonAssistant = new JsonAssistant();
    // 所有收的数据都会在这里
    public static boolean isdispose = true;

    @Override
    public void analy(byte[] buffer) {
        String data = new String(buffer).trim();
        Log.i(TAG, "client收到：" + data);
        try {
            if (data.contains("BSboot")) {
                //isdispose=true;
                // 发广播
                Intent intent = new Intent();
                intent.setAction(MouseBusinessServiceImpl.DAO_TCPIP_TCPIPSERVER);
                intent.putExtra(MouseBusinessServiceImpl.CMD, "boot");
                intent.putExtra("boot", jsonAssistant.getBoot(data));
                // 发送自定义无序广播
                App.getAppContext().sendBroadcast(intent);
            } else if (data.contains("cmd") && data.contains("BSgetsonglist")) {
                Intent intent = new Intent();
                intent.setAction(MediaMusicActivity.MBROADCASTRECEIVER);
                intent.putExtra(MusicBusinessServiceImpl.CMD, "BSgetsonglist");
                intent.putExtra("BSgetsonglist", jsonAssistant.paseGetSongList(data));
                // 发送自定义无序广播
                App.getAppContext().sendBroadcast(intent);
            } else if (data.contains("cmd") && data.contains("BSsetplaystatus")) {
                //client收到：{"cmd":"BSsetplaystatus","status":"play"}
                Intent intent = new Intent();
                intent.setAction(MediaMusicActivity.MBROADCASTRECEIVER);
                intent.putExtra(MusicBusinessServiceImpl.CMD, "BSsetplaystatus");
                intent.putExtra("setplaystatus", jsonAssistant.paseSetplaystatus(data));
                // 发送自定义无序广播
                App.getAppContext().sendBroadcast(intent);
            } else if (data.contains("cmd") && data.contains("BSsetmode")) {
                Intent intent = new Intent();
                intent.setAction(MediaMusicActivity.MBROADCASTRECEIVER);
                intent.putExtra("setmode", jsonAssistant.paseSetmode(data));
                intent.putExtra(MusicBusinessServiceImpl.CMD, "BSsetmode");
                // 发送自定义无序广播
                App.getAppContext().sendBroadcast(intent);
            } else if (data.contains("cmd") && data.contains("BSgetvideolist")) {
                Intent intent = new Intent();
                intent.setAction(MediaVideoActivity.MBROADCASTRECEIVER);
                intent.putExtra(MusicBusinessServiceImpl.CMD, "BSgetvideolist");
                intent.putExtra("BSgetvideolist", jsonAssistant.paseGetVideoList(data));
                // 发送自定义无序广播
                App.getAppContext().sendBroadcast(intent);
            } else if (data.contains("cmd") && data.contains("BSsetvideoplaystatus")) {
                Intent intent = new Intent();
                intent.setAction(MediaVideoActivity.MBROADCASTRECEIVER);
                intent.putExtra(VideoBusinessServiceImpl.CMD, "BSsetvideoplaystatus");
                intent.putExtra("setvideoplaystatus", jsonAssistant.paseSetvideoplaystatus(data));
                // 发送自定义无序广播
                App.getAppContext().sendBroadcast(intent);
            } else if (data.contains("cmd") && data.contains("BSgetimageFolderList")) {
                Intent intent = new Intent();
                intent.setAction(MediaImageFolderActivity.MBROADCASTRECEIVER);
                intent.putExtra(ImageBusinessServiceImpl.CMD, "BSgetimageFolderList");
                intent.putExtra("getimageFolderList", jsonAssistant.paseGetimageFolderList(data));
                // 发送自定义无序广播
                App.getAppContext().sendBroadcast(intent);
            } else if (data.contains("cmd") && data.contains("BSgetimageList")) {
                Intent intent = new Intent();
                intent.setAction(MediaImageActivity.MBROADCASTRECEIVER);
                intent.putExtra(ImageBusinessServiceImpl.CMD, "BSgetimageList");
                intent.putExtra("getimageList", jsonAssistant.paseGetimageList(data));
                // 发送自定义无序广播
                App.getAppContext().sendBroadcast(intent);
            }
//			else if(data.contains("cmd")&&data.contains("BSgetfile")){
//				//{"cmd":"BSgetfile","fileName":"/mnt/internal_sd"}
//				Intent intent = new Intent();
//				intent.setAction(FileActivity.MBROADCASTRECEIVER);
//				intent.putExtra("cmd","BSgetfile");
//				intent.putExtra("BSgetfile", jsonAssistant.paseGetfile(data));
//				// 发送自定义无序广播
//				App.getAppContext().sendBroadcast(intent);
//			}
            else if (data.contains("cmd") && data.contains("BSopenZyglq")) {
                Intent intent = new Intent();
                intent.setAction(FileActivity.MBROADCASTRECEIVER);
                intent.putExtra(FileBusinessServiceImpl.CMD, "BSopenZyglq");
                intent.putExtra("openZyglq", jsonAssistant.paseOpenZyglq(data));
                App.getAppContext().sendBroadcast(intent);
            } else if (data.contains("cmd") && data.contains("BSgetfileCategoryList")) {
                Intent intent = new Intent();
                intent.setAction(FileActivity.MBROADCASTRECEIVER);
                intent.putExtra(FileBusinessServiceImpl.CMD, "BSgetfileCategoryList");
                intent.putExtra("fileCategoryList", jsonAssistant.paseFileCategoryList(data));
                App.getAppContext().sendBroadcast(intent);
            } else if (data.contains("cmd") && data.contains("BSopenfileid")) {
                Intent intent = new Intent();
                intent.setAction(FileActivity.MBROADCASTRECEIVER);
                intent.putExtra(FileBusinessServiceImpl.CMD, "BSopenfileid");
                intent.putExtra("openfileid", jsonAssistant.paseOpenfileid(data));
                App.getAppContext().sendBroadcast(intent);
            } else if (data.contains("cmd") && data.contains("BSopenFileCategory")) {
                Intent intent = new Intent();
                intent.setAction(FileActivity.MBROADCASTRECEIVER);
                intent.putExtra(FileBusinessServiceImpl.CMD, "BSopenFileCategory");
                intent.putExtra("openFileCategory", jsonAssistant.paseOpenFileCategory(data));
                App.getAppContext().sendBroadcast(intent);
            } else if (data.contains("cmd") && data.contains("BSfileShowList")) {
                Intent intent = new Intent();
                intent.setAction(FileShowListActivity.MBROADCASTRECEIVER);
                intent.putExtra(FileBusinessServiceImpl.CMD, "BSfileShowList");
                intent.putExtra("fileShowList", jsonAssistant.paseFileShowList(data));
                App.getAppContext().sendBroadcast(intent);
            }
//			Log.i(TAG, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
