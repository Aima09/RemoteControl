package com.yuanfang.intercom.service;

import com.yuanfang.intercom.service.IIntercomCallback;

interface IIntercomService {

    void startRecord(String ipAddress);
    void stopRecord();
    void leaveGroup();
    void registerCallback(IIntercomCallback callback);
    void unRegisterCallback(IIntercomCallback callback);
}
