package com.yf.remotecontrolserver.common.ui.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.yf.remotecontrolserver.zyglq.FileBusinessService;
import com.yf.remotecontrolserver.zyglq.FileBusinessServiceImpl;

public class FileReceiver extends BroadcastReceiver {
    private final String TAG = "FileReceiver";
    FileBusinessService fileBusinessService = new FileBusinessServiceImpl();
    //play图片的数据key
    public static final String FILE_KEY = "file_key";

    @Override
    public void onReceive(Context context, Intent intent) {
        String data = intent.getStringExtra(FILE_KEY);
        Log.i(TAG, data);
        //fileBusinessService.sendFile(data);
    }
}