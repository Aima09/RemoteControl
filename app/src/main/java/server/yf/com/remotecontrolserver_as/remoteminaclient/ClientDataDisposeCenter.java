package server.yf.com.remotecontrolserver_as.remoteminaclient;


import com.yf.minalibrary.common.PreferenceUtils;

import server.yf.com.remotecontrolserver_as.App;

/**
 * Created by wuhuai on 2017/6/27 .
 * ;
 */

public class ClientDataDisposeCenter {

    public static void setLocalSenderId(String senderId) {
        PreferenceUtils.setPref(App.getAppContext(), Constants.KEY_SENDER_ID, senderId);
    }

    public static String getLocalSenderId() {
        return PreferenceUtils.getPrefString(App.getAppContext(), Constants.KEY_SENDER_ID, "");
    }

    public static void setRemoteReceiverId(String remoteReceiverId) {
        PreferenceUtils.setPref(App.getAppContext(), Constants.KEY_REVEIVER_ID, remoteReceiverId);
    }

    public static String getRemoteReceiverId() {
        return PreferenceUtils.getPrefString(App.getAppContext(), Constants.KEY_REVEIVER_ID, "");
    }


}
