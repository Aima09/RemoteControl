package server.yf.com.remotecontrolserver_as.util;


import server.yf.com.remotecontrolserver_as.App;
import server.yf.com.remotecontrolserver_as.domain.Equipment;

public class EquipmentFactory {
	public static Equipment getEquipment(){
		Equipment equipment=new Equipment();
		equipment.setDevid("123456");
		equipment.setIp(IpUtil.getLocalIpAddress(App.getAppContext()));
		return equipment;
	}
}
