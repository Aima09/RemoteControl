package com.yf.remotecontrolserver.remoteminaclient;


import android.util.Log;

import com.yf.minalibrary.message.IntercomMessage;
import com.yf.minalibrary.message.IntercomMessage.IntercomBean;
import com.yuanfang.intercom.data.AudioData;
import com.yuanfang.intercom.data.MessageQueue;

import java.util.Arrays;

import static com.yf.remotecontrolserver.common.ui.serice.MouseService.TAG;


/**
 * Created by wuhuai on 2017/6/23 .
 * ;
 */

public class ClientMinaIntercomManager {
    private static ClientMinaIntercomManager instance;

    public static synchronized ClientMinaIntercomManager getInstance() {
        if (instance == null) {
            synchronized (ClientMinaIntercomManager.class) {
                if (instance == null)
                    instance = new ClientMinaIntercomManager();
            }
        }
        return instance;
    }

    public void disposeIntercom(IntercomMessage intercomMessage) {
        IntercomBean intercomBean = intercomMessage.getIntercomBean();
        AudioData audioData = new AudioData(intercomBean.getIntercomContent());
        MessageQueue.getInstance(MessageQueue.DECODER_DATA_QUEUE).put(audioData);
    }

}
