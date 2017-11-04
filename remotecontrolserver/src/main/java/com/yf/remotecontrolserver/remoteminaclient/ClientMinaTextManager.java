package com.yf.remotecontrolserver.remoteminaclient;


import com.yf.minalibrary.message.TextMessage;


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
