package server.yf.com.remotecontrolserver_as.minamachines;


import android.text.TextUtils;
import android.util.Log;

import com.yf.minalibrary.common.PreferenceUtils;

import server.yf.com.remotecontrolserver_as.App;

/**
 * Created by wuhuai on 2017/6/27 .
 * ;
 */

public class ServerDataDisposeCenter {

    public static void addDevice(DeviceInfo deviceInfo) {
        String allDevice = PreferenceUtils.getPrefString(App.getAppContext(), Constants.KEY_DEVICE_LIST, "");
        String info = deviceInfo.getId() + ":" + deviceInfo.getName();
        if (!TextUtils.isEmpty(allDevice)) {
            String[] allDevices = allDevice.split(",");
            for (String allDevice1 : allDevices) {
                if (allDevice1.split(":")[0].equals(deviceInfo.getId())) {
                    // TODO: 2017/6/27 处理，通知该设备已经被添加,不要重复添加
                    return;
                }
            }
            allDevice += "," + info;
        } else {
            allDevice = info;
        }
        PreferenceUtils.setPref(App.getAppContext(), Constants.KEY_DEVICE_LIST, allDevice);
        DevicesManager.getInstance().updateDevicesInfoList();
    }

    public static void removeDevice(DeviceInfo deviceInfo) {
        String allDevice = PreferenceUtils.getPrefString(App.getAppContext(), Constants.KEY_DEVICE_LIST, "");
        if (!TextUtils.isEmpty(allDevice)) {
            String[] allDevices = allDevice.split(",");
            allDevice = "";
            int j = 0;
            for (int i = 0; i < allDevices.length; i++) {
                String allDevice1 = allDevices[i];
                if (!allDevice1.split(":")[0].equals(deviceInfo.getId())) {
                    if (j > 0){
                        allDevice += "," + allDevice1;
                    } else {
                        allDevice += allDevice1;
                    }
                    j++;
                    Log.d("ServerDataDisposeCenter", "j:" + j);
                }
            }
        }
        PreferenceUtils.setPref(App.getAppContext(), Constants.KEY_DEVICE_LIST, allDevice);
        DevicesManager.getInstance().updateDevicesInfoList();
    }

    public static void renameDeviceName(DeviceInfo deviceInfo) {
        String allDevice = PreferenceUtils.getPrefString(App.getAppContext(), Constants.KEY_DEVICE_LIST, "");
        if (!TextUtils.isEmpty(allDevice)) {
            String[] allDevices = allDevice.split(",");
            allDevice = "";
            for (int i = 0; i < allDevices.length; i++) {
                String allDevice1 = allDevices[i];
                if (allDevice1.split(":")[0].equals(deviceInfo.getId())) {
                    allDevice1 = deviceInfo.getId() + ":" + deviceInfo.getName();
                }
                if (i > 0) {
                    allDevice += "," + allDevice1;
                } else {
                    allDevice += allDevice1;
                }
            }
        }
        PreferenceUtils.setPref(App.getAppContext(), Constants.KEY_DEVICE_LIST, allDevice);
        DevicesManager.getInstance().updateDevicesInfoList();
    }

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
