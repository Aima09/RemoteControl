package com.yf.remotecontrolserver.localminaserver;


import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.yf.minalibrary.common.CmdType;
import com.yf.minalibrary.common.DeviceType;
import com.yf.minalibrary.common.FileMessageConstant;
import com.yf.minalibrary.common.MessageType;
import com.yf.minalibrary.message.CmdMessage;
import com.yf.minalibrary.message.FileMessage;
import com.yf.minalibrary.message.IntercomMessage;
import com.yf.remotecontrolserver.common.App;
import com.yf.remotecontrolserver.dao.TcpAnalyzerImpl;
import com.yuanfang.intercom.data.AudioData;
import com.yuanfang.intercom.data.MessageQueue;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;

/**
 * Created by wuhuai on 2017/6/23 .
 * ;
 */

public class LocalMinaMassageManager {

    private static LocalMinaMassageManager instance;
    private LocalMinaServerController localMinaServerController;

    public static synchronized LocalMinaMassageManager getInstance() {
        if (instance == null) {
            synchronized (LocalMinaMassageManager.class) {
                if (instance == null)
                    instance = new LocalMinaMassageManager();
            }
        }
        return instance;
    }

    public void setLocalMinaServerController(LocalMinaServerController localMinaServerController) {
        this.localMinaServerController = localMinaServerController;
    }

    public void disposeCmd(CmdMessage cmdMessage) {
        String cmdType = cmdMessage.getCmdType();
        switch (cmdType) {
            case CmdType.CMD_MUSIC:
                TcpAnalyzerImpl.getInstans().analy(cmdMessage.getCmdContent().getBytes(), null);
                break;
            case CmdType.CMD_SECURITY:
                break;
        }
    }

    public void disposeFile(FileMessage fileMessage) {
        if (fileMessage.getUse().equals(FileMessageConstant.UPLOAD_MUSIC)) {
            try {
                Log.d("LocalMinaMassageManager", "Received filename = " + fileMessage.getFileName());
                File file = null;
                if (fileMessage.getFileName().endsWith(".mp3")) {
                    file = new File(Environment.getExternalStorageDirectory() + "/yinyue");
                } else if (fileMessage.getFileName().endsWith(".png") || fileMessage.getFileName().endsWith(".jpg")) {
                    file = new File(Environment.getExternalStorageDirectory() + "/tupian");
                }
                boolean b = file.exists();
                if (!b) {
                    b = file.mkdir();
                }
                if (b) {
                    FileOutputStream os = new FileOutputStream(file.getPath() + "/" + fileMessage.getFileName());
                    os.write(fileMessage.getFileContent());
                    os.close();
                    fileScan(file.getPath() + "/" + fileMessage.getFileName());
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    ComponentName cn = new ComponentName("player.yf.com.player", "player.yf.com.player.MainActivity");
                    intent.setComponent(cn);
                    App.getAppContext().startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (fileMessage.getUse().equals(FileMessageConstant.ON_LINE_MUSIC)) {
            try {
                Log.d("LocalMinaMassageManager", "Received filename = " + fileMessage.getFileName());
                File file = null;
                if (fileMessage.getFileName().endsWith(".mp3")) {
                    file = new File(Environment.getExternalStorageDirectory() + "/yinyue");
                } else if (fileMessage.getFileName().endsWith(".png") || fileMessage.getFileName().endsWith(".jpg")) {
                    file = new File(Environment.getExternalStorageDirectory() + "/tupian");
                }
                boolean b = file.exists();
                if (!b) {
                    b = file.mkdir();
                }
                if (b) {
                    FileOutputStream os = new FileOutputStream(file.getPath() + "/" + fileMessage.getFileName(),true);
                    os.write(fileMessage.getFileContent()[fileMessage.getFileContent().length - 1]);
                    os.close();
                    if (fileMessage.getFileContent().length == 500) {
                        fileScan(file.getPath() + "/" + fileMessage.getFileName());
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        ComponentName cn = new ComponentName("player.yf.com.player", "player.yf.com.player.MainActivity");
                        intent.setComponent(cn);
                        App.getAppContext().startActivity(intent);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void fileScan(String file) {
        Uri data = Uri.parse("file://" + file);
        Log.d("TAG", "file:" + file);
        App.getInstance().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, data));
    }

    public void disposeIntercom(IntercomMessage intercomMessage) {
        AudioData audioData = new AudioData(Base64.decode(intercomMessage.getIntercomContent(), Base64.DEFAULT));
        MessageQueue.getInstance(MessageQueue.DECODER_DATA_QUEUE).put(audioData);
    }

    public void sendControlCmd(String cmdContent) {
        sendControlCmd(CmdType.CMD_MUSIC, cmdContent);
    }

    public void sendControlCmd(String cmdType, String cmdContent) {
        if (null != localMinaServerController) {
            Log.i("LocalMinaMassageManager", "发送数据");
            CmdMessage cmdMessage = new CmdMessage(null, null, MessageType.MESSAGE_CMD, cmdType, DeviceType.DEVICE_TYPE_IPAD, cmdContent);
            localMinaServerController.send(cmdMessage);
        }
    }
}
