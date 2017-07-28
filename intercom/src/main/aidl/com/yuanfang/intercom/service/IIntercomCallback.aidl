package com.yuanfang.intercom.service;

interface IIntercomCallback {

    void findNewUser(String ipAddress);
    void removeUser(String ipAddress);
}
