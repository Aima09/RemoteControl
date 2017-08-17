package com.yf.remotecontrolserver.dao;


import android.content.Intent;
import android.util.Log;

import com.yf.remotecontrolserver.common.App;
import com.yf.remotecontrolserver.domain.Action;
import com.yf.remotecontrolserver.domain.Writer;
import com.yf.remotecontrolserver.mouse.MouseBusinessServiceImpl;
import com.yf.remotecontrolserver.util.JsonAssistant;

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
    // 动作
    public static final String SERVICE_MOUSE = "com.syf.mousekey.SERVICE_MOUSE";
    public static final String MOUSE_key = "MOUSE_key";
    public static final String SERVICE_KEY = "com.yf.mousekey.SERVICE_KEY";
    public static final String KEY_key = "KEY_key";

    public static final String MOUSE_MODE = "com.yf.mousekey.MOUSE_MODE";
    public static final String MODE_key = "MODE_key";

    public static int mMouseMode;// 模式

    // 音乐的数据key
    public static final String MUSIC_KEY = "music_key";
    // 音乐的action
    public static final String MUSICRECEIVER = "COM.YUANFANG.YINYUE.UI.RECEIVER.MUSICRECEIVER";

    // 视频的数据key
    public static final String VIDEO_KEY = "VIDEO_KEY";
    public static final String VIDEORECEIVER = "COM.YUANFANG.YINYUE.UI.RECEIVER.VIDEORECEIVER";

    // 图片的数据key
    public static final String IMAGE_KEY = "image_key";
    public static final String IMAGERECEIVER = "com.yuanfang.yinyue.ui.receiver.ImageReceiver";

    // 浏览器数据key
    public static final String BROWSER_KEY = "browser_key";
    public static final String BROWSERRECEIVER = "com.yuanfang.yinyue.ui.receiver.BrowserReceiver";

    // 设置数据key
    public static final String SETTINGS_KEY = "Settings_key";
    public static final String SETTINGSRECEIVER = "com.yuanfang.yinyue.ui.receiver.SettingsReceiver";

    // 资源管理器数据key
    public static final String ZYGLQ_KEY = "Zyglq_key";
    public static final String ZYGLQSRECEIVER = "com.app.main.ifileexplorer.receiver.ZyglqReceiver";

    @Override
    public void analy(byte[] buffer, String receiverId) {
        String data = new String(buffer).trim();
        Log.i(TAG, data);
        try {
            // 优先处理所以放到最前面（优化）
            if (data.contains("cmd") && data.contains("write")
                    && data.contains("data")) {
                // 收到写入者
                Writer writer = jsonAssistant.paseWriter(data);
                Intent intent = new Intent();
                intent.setAction(MouseBusinessServiceImpl.DAO_TCPIP_TCPIPSERVER);
                intent.putExtra(MouseBusinessServiceImpl.CMD, writer.getCmd());
                intent.putExtra("writer", writer);
                App.getAppContext().sendBroadcast(intent);
                return;
            } else if (data.contains("cmd") && data.contains("data")) {
                // 接收到了动作
                Action action = jsonAssistant.paseAction(data);
                Intent intent = new Intent();
                intent.setAction(MouseBusinessServiceImpl.DAO_TCPIP_TCPIPSERVER);
                intent.putExtra(MouseBusinessServiceImpl.CMD, "action");
                intent.putExtra("action", action);
                // 发送自定义无序广播
                App.getAppContext().sendBroadcast(intent);
            } else if (data.contains("cmd") && data.contains("BSgetsonglist")) {
                // 读数据{"pageindex":0,"pagesize":20,"cmd":"BSgetsonglist"}
                Intent intent = new Intent();
                intent.setAction(MUSICRECEIVER);
                intent.putExtra(MUSIC_KEY, data);
                intent.putExtra("receiverId", receiverId);
                App.getAppContext().sendBroadcast(intent);
                // Log.i(TAG, "send"+data);
            } else if (data.contains("cmd") && data.contains("BSsetplaysongid")) {
                // {"cmd":"BSsetplaysongid","songid":4}
                Intent intent = new Intent();
                intent.setAction(MUSICRECEIVER);
                intent.putExtra(MUSIC_KEY, data);
                intent.putExtra("receiverId", receiverId);
                App.getAppContext().sendBroadcast(intent);
            }else if(data.contains("cmd") && data.contains("BSsetplaysongProgress")){
                Intent intent = new Intent();
                intent.setAction(MUSICRECEIVER);
                intent.putExtra(MUSIC_KEY, data);
                intent.putExtra("receiverId", receiverId);
                App.getAppContext().sendBroadcast(intent);
            }else if(data.contains("cmd") && data.contains("BSgetsongstatus")){
                Intent intent = new Intent();
                intent.setAction(MUSICRECEIVER);
                intent.putExtra(MUSIC_KEY, data);
                intent.putExtra("receiverId", receiverId);
                App.getAppContext().sendBroadcast(intent);
            }else if (data.contains("cmd") && data.contains("BSsetvolumeadd")) {
                // {"cmd":"BSsetvolumeadd","valume":"-"}
                // 声音调低
                Intent intent = new Intent();
                intent.setAction(MUSICRECEIVER);
                intent.putExtra(MUSIC_KEY, data);
                intent.putExtra("receiverId", receiverId);
                App.getAppContext().sendBroadcast(intent);
            } else if (data.contains("cmd") && data.contains("BSsetplaystatus")) {
                // {"cmd":"BSsetplaystatus","status":"previous"}
                // {"cmd":"BSsetplaystatus","status":"start_pause"}
                // {"cmd":"BSsetplaystatus","status":"next"}
                Intent intent = new Intent();
                intent.setAction(MUSICRECEIVER);
                intent.putExtra(MUSIC_KEY, data);
                intent.putExtra("receiverId", receiverId);
                App.getAppContext().sendBroadcast(intent);
            } else if (data.contains("cmd") && data.contains("BSgetvideolist")) {
                Intent intent = new Intent();
                intent.setAction(VIDEORECEIVER);
                intent.putExtra(VIDEO_KEY, data);
                intent.putExtra("receiverId", receiverId);
                App.getAppContext().sendBroadcast(intent);
            } else if (data.contains("cmd")
                    && data.contains("BSsetplayvideoid")) {
                Intent intent = new Intent();
                intent.setAction(VIDEORECEIVER);
                intent.putExtra(VIDEO_KEY, data);
                intent.putExtra("receiverId", receiverId);
                App.getAppContext().sendBroadcast(intent);
            } else if (data.contains("cmd")
                    && data.contains("BSsetvideoplaystatus")) {
                Intent intent = new Intent();
                intent.setAction(VIDEORECEIVER);
                intent.putExtra(VIDEO_KEY, data);
                intent.putExtra("receiverId", receiverId);
                App.getAppContext().sendBroadcast(intent);
            } else if (data.contains("cmd")
                    && data.contains("BSsetvideovolumeadd")) {
                Intent intent = new Intent();
                intent.setAction(VIDEORECEIVER);
                intent.putExtra(VIDEO_KEY, data);
                intent.putExtra("receiverId", receiverId);
                App.getAppContext().sendBroadcast(intent);
            } else if (data.contains("cmd") && data.contains("BSsetmode")) {
                Intent intent = new Intent();
                intent.setAction(MUSICRECEIVER);
                intent.putExtra(MUSIC_KEY, data);
                intent.putExtra("receiverId", receiverId);
                App.getAppContext().sendBroadcast(intent);
            } else if (data.contains("cmd")
                    && data.contains("BSgetimageFolderList")) {
                // {"cmd":"BSgetimageFolderList","pageIndex":0,"pageSize":2,"total":0}
                Intent intent = new Intent();
                intent.setAction(IMAGERECEIVER);
                intent.putExtra(IMAGE_KEY, data);
                intent.putExtra("receiverId", receiverId);
                App.getAppContext().sendBroadcast(intent);
            } else if (data.contains("cmd")
                    && data.contains("BSopenImageFolder")) {
                Intent intent = new Intent();
                intent.setAction(IMAGERECEIVER);
                intent.putExtra(IMAGE_KEY, data);
                intent.putExtra("receiverId", receiverId);
                App.getAppContext().sendBroadcast(intent);
            } else if (data.contains("cmd") && data.contains("BSgetimageList")) {
                // {"cmd":"BSgetimageList","pageIndex":0,"pageSize":2,"total":0}
                Intent intent = new Intent();
                intent.setAction(IMAGERECEIVER);
                intent.putExtra(IMAGE_KEY, data);
                intent.putExtra("receiverId", receiverId);
                App.getAppContext().sendBroadcast(intent);
            } else if (data.contains("cmd") && data.contains("BSopenImage")) {
                Intent intent = new Intent();
                intent.setAction(IMAGERECEIVER);
                intent.putExtra(IMAGE_KEY, data);
                App.getAppContext().sendBroadcast(intent);
            } else if (data.contains("BSopenBrower")) {
                Intent intent = new Intent();
                intent.setAction(BROWSERRECEIVER);
                intent.putExtra(BROWSER_KEY, data);
                intent.putExtra("receiverId", receiverId);
                App.getAppContext().sendBroadcast(intent);
            } else if (data.contains("BSopenSettings")) {
                Intent intent = new Intent();
                intent.setAction(SETTINGSRECEIVER);
                intent.putExtra(SETTINGS_KEY, data);
                intent.putExtra("receiverId", receiverId);
                App.getAppContext().sendBroadcast(intent);
            } else if (data.contains("BSopenZyglq")) {
                Intent intent = new Intent();
                intent.setAction(ZYGLQSRECEIVER);
                intent.putExtra(ZYGLQ_KEY, data);
                intent.putExtra("receiverId", receiverId);
                App.getAppContext().sendBroadcast(intent);
            } else if (data.contains("BSgetfileCategoryList")) {
                Intent intent = new Intent();
                intent.setAction(ZYGLQSRECEIVER);
                intent.putExtra(ZYGLQ_KEY, data);
                intent.putExtra("receiverId", receiverId);
                App.getAppContext().sendBroadcast(intent);
            } else if (data.contains("BSopenfileid")) {
                Intent intent = new Intent();
                intent.setAction(ZYGLQSRECEIVER);
                intent.putExtra(ZYGLQ_KEY, data);
                intent.putExtra("receiverId", receiverId);
                App.getAppContext().sendBroadcast(intent);
            } else if (data.contains("BSopenFileCategory")) {
                Intent intent = new Intent();
                intent.setAction(ZYGLQSRECEIVER);
                intent.putExtra(ZYGLQ_KEY, data);
                intent.putExtra("receiverId", receiverId);
                App.getAppContext().sendBroadcast(intent);
            } else if (data.contains("BSfileShowList")) {
                Intent intent = new Intent();
                intent.setAction(ZYGLQSRECEIVER);
                intent.putExtra(ZYGLQ_KEY, data);
                intent.putExtra("receiverId", receiverId);
                App.getAppContext().sendBroadcast(intent);
            } else if (data.contains("BSopenFile")) {
                Intent intent = new Intent();
                intent.setAction(ZYGLQSRECEIVER);
                intent.putExtra(ZYGLQ_KEY, data);
                intent.putExtra("receiverId", receiverId);
                App.getAppContext().sendBroadcast(intent);
            } else if (data.contains("BSfinishIFileExplorerCommonActivity")) {
                Intent intent = new Intent();
                intent.setAction(ZYGLQSRECEIVER);
                intent.putExtra(ZYGLQ_KEY, data);
                intent.putExtra("receiverId", receiverId);
                App.getAppContext().sendBroadcast(intent);
            }
            // Log.i(TAG, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
