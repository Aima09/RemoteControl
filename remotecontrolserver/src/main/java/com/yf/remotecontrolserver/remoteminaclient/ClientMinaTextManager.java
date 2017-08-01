package com.yf.remotecontrolserver.remoteminaclient;


import android.os.Environment;
import android.util.Log;

import com.yf.minalibrary.message.FileMessage;
import com.yf.minalibrary.message.TextMessage;

import java.io.File;
import java.io.FileOutputStream;

import static com.yf.remotecontrolserver.common.ui.serice.MouseService.TAG;


/**
 * Created by wuhuai on 2017/6/23 .
 * ;
 */

public class ClientMinaTextManager {
    private static ClientMinaTextManager instance;

    public static synchronized ClientMinaTextManager getInstance() {
        if (instance == null) {
            synchronized (ClientMinaTextManager.class) {
                if (instance == null)
                    instance = new ClientMinaTextManager();
            }
        }
        return instance;
    }

    public void disposeText(TextMessage textMessage) {
        
    }
}
