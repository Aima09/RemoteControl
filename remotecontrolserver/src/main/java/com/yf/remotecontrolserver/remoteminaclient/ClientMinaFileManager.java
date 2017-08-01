package com.yf.remotecontrolserver.remoteminaclient;


import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.yf.minalibrary.message.FileMessage;
import com.yf.remotecontrolserver.common.App;

import java.io.File;
import java.io.FileOutputStream;

import static com.yf.remotecontrolserver.common.config_server.TAG;


/**
 * Created by wuhuai on 2017/6/23 .
 * ;
 */

public class ClientMinaFileManager {
    private static ClientMinaFileManager instance;

    public static synchronized ClientMinaFileManager getInstance() {
        if (instance == null) {
            synchronized (ClientMinaFileManager.class) {
                if (instance == null)
                    instance = new ClientMinaFileManager();
            }
        }
        return instance;
    }

    public void disposeFile(FileMessage fileMessage) {
        try {
            FileMessage.FileBean bean = fileMessage.getFileBean();
            Log.d("ClientMinaFileManager", "Received filename = " + bean.getFileName());
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
}
