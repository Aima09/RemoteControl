package com.yf.remotecontrolclient.minaclient.tcp;

import android.media.MediaDataSource;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.yf.minalibrary.common.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import com.yf.remotecontrolclient.App;

/**
 * Created by wuhuai on 2016/11/15 .
 * ;
 */

public class DevicesManager {
    private static DevicesManager instance;
    private List<DeviceInfo> deviceInfos = new ArrayList<>();
    private List<DevicesUpdateListener> devicesUpdateListeners = new ArrayList<>();

    public DevicesManager() {
        initDevicesInfoList();
    }

    public static synchronized DevicesManager getInstance() {
        if (instance == null) {
            synchronized (DevicesManager.class) {
                if (instance == null)
                    instance = new DevicesManager();
            }
        }
        return instance;
    }

    public void addDevicesUpdateListener(DevicesUpdateListener listener) {
        if (!devicesUpdateListeners.contains(listener)) {
            devicesUpdateListeners.add(listener);
        }
    }

    public void removeDevicesUpdateListener(DevicesUpdateListener listener) {
        if (devicesUpdateListeners.contains(listener)) {
            devicesUpdateListeners.remove(listener);
        }
    }

    public void exeDevicesUpdate() {
        for (DevicesUpdateListener listener : devicesUpdateListeners) {
            listener.devicesUpdate();
        }
    }

    private void initDevicesInfoList() {
        deviceInfos.clear();
        String device = PreferenceUtils.getPrefString(App.getAppContext(), Constants.KEY_DEVICE_LIST, "");
        Log.d("DevicesManager", device);
        if (!TextUtils.isEmpty(device)) {
            String[] deviceStrings = device.split(",");
            for (String deviceString : deviceStrings) {
                DeviceInfo deviceInfo = new DeviceInfo();
                deviceInfo.setId(deviceString.split(":")[0]);
                deviceInfo.setName(deviceString.split(":")[1]);
                deviceInfos.add(deviceInfo);
            }
        }
    }

    public void updateDevicesInfoList() {
        initDevicesInfoList();
        exeDevicesUpdate();
    }

    public List<DeviceInfo> getDeviceInfos() {
        return deviceInfos;
    }

    public interface DevicesUpdateListener {
        void devicesUpdate();
    }
}
