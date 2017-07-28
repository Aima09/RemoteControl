package com.yf.remotecontrolserver.util;


import com.yf.remotecontrolserver.common.App;
import com.yf.remotecontrolserver.domain.Equipment;

public class EquipmentFactory {
    public static Equipment getEquipment() {
        Equipment equipment = new Equipment();
        equipment.setDevid("123456");
        equipment.setIp(IpUtil.getLocalIpAddress(App.getAppContext()));
        return equipment;
    }
}
