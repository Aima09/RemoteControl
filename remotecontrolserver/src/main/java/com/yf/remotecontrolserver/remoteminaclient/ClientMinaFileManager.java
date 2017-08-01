package com.yf.remotecontrolserver.remoteminaclient;


import android.os.Environment;
import android.util.Log;

import com.yf.minalibrary.message.FileMessage;

import java.io.File;
import java.io.FileOutputStream;

import static com.yf.remotecontrolserver.common.ui.serice.MouseService.TAG;


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
            Log.d(TAG, "Received filename = " + bean.getFileName());
            File file = new File(Environment.getExternalStorageDirectory() + "/tupian");
            boolean b = file.exists();
            if (!b) {
                b = file.mkdir();
            }
            if (b) {
                FileOutputStream os = new FileOutputStream(file.getPath() + "/" + bean.getFileName());
                os.write(bean.getFileContent());
                os.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
