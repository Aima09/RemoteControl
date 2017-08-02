package com.yf.remotecontrolserver.localminaserver;


import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.yf.minalibrary.common.CmdType;
import com.yf.minalibrary.common.DeviceType;
import com.yf.minalibrary.common.MessageType;
import com.yf.minalibrary.message.CmdMessage;
import com.yf.minalibrary.message.CmdMessage.CmdBean;
import com.yf.minalibrary.message.FileMessage;
import com.yf.minalibrary.message.IntercomMessage;
import com.yf.minalibrary.message.IntercomMessage.IntercomBean;
import com.yf.remotecontrolserver.common.App;
import com.yf.remotecontrolserver.dao.TcpAnalyzerImpl;
import com.yuanfang.intercom.data.AudioData;
import com.yuanfang.intercom.data.MessageQueue;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        CmdBean cmdBean = cmdMessage.getCmdBean();
        String cmdType = cmdBean.getCmdType();
        switch (cmdType) {
            case CmdType.CMD_MUSIC:
                Log.d("LocalMinaMassageManager", "接收到音乐命令：" + cmdBean.getCmdContent());
                TcpAnalyzerImpl.getInstans().analy(cmdBean.getCmdContent().getBytes(), null);
                break;
            case CmdType.CMD_SECURITY:
                break;
        }
    }

    public void disposeFile(FileMessage fileMessage) {
        try {
            FileMessage.FileBean bean = fileMessage.getFileBean();
            Log.d("LocalMinaMassageManager", "Received filename = " + bean.getFileName());
            File file = new File(Environment.getExternalStorageDirectory() + "/tupian");
            boolean b = file.exists();
            if (!b) {
                b = file.mkdir();
            }
            if (b) {
                FileOutputStream os = new FileOutputStream(file.getPath() + "/" + bean.getFileName());
                os.write(bean.getFileContent());
                os.close();
                Intent it = new Intent(Intent.ACTION_VIEW);
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Uri mUri = Uri.parse("file://" + file.getPath() + "/" + bean.getFileName());
                it.setDataAndType(mUri, "image/*");
                App.getAppContext().startActivity(it);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disposeIntercom(IntercomMessage intercomMessage) {
        IntercomBean intercomBean = intercomMessage.getIntercomBean();
        Log.d("LocalMinaMassageManager", "MessageQueue.getInstance " + Arrays.toString(
                Base64.decode(intercomBean.getIntercomContent(),Base64.DEFAULT)));
        AudioData audioData = new AudioData(Base64.decode(intercomBean.getIntercomContent(),Base64.DEFAULT));
        MessageQueue.getInstance(MessageQueue.DECODER_DATA_QUEUE).put(audioData);
    }

    public void sendControlCmd(String cmdContent) {
        sendControlCmd(CmdType.CMD_MUSIC,cmdContent);
    }

    public void sendControlCmd(String cmdType,String cmdContent) {
        if (null != localMinaServerController) {
            Log.i("LocalMinaMassageManager", "发送数据");
            CmdBean cmdBean = new CmdBean(cmdType, DeviceType.DEVICE_TYPE_IPAD, cmdContent);
            CmdMessage cmdMessage = new CmdMessage(MessageType.MESSAGE_CMD, cmdBean);
            localMinaServerController.send(cmdMessage);
        }
    }
}
