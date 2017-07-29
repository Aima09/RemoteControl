package com.yf.remotecontrolclient.util;

import android.content.Intent;

import com.yf.remotecontrolclient.App;
import com.yf.remotecontrolclient.CommonConstant;

/**
 * Created by sujuntao on 2017/7/5.
 */

public class PromptUtil {
    public static void connectionstatus(String statusString) {
        SpUtil.putString(App.getAppContext(),
                CommonConstant.CONNECTION_KEY, statusString);
        Intent intent = new Intent();
        intent.setAction(CommonConstant.BASEACTIVITY_MBASEACTIVITYBROADCASTRECEIVER);
        intent.putExtra("data", statusString);
        App.getAppContext().sendBroadcast(intent);
    }
}
