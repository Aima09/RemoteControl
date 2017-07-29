package com.yf.remotecontrolclient.dao;


import android.content.Intent;

import java.util.List;

import com.yf.remotecontrolclient.App;
import com.yf.remotecontrolclient.activity.ChooseRoomActivity;
import com.yf.remotecontrolclient.activity.service.MouseService;
import com.yf.remotecontrolclient.domain.Equipment;
import com.yf.remotecontrolclient.domain.Palpitation;
import com.yf.remotecontrolclient.service.imp.MouseBusinessServiceImpl;
import com.yf.remotecontrolclient.util.IpUtil;
import com.yf.remotecontrolclient.util.JsonAssistant;

public class UdpAnalyzerImpl implements AnalyzerInterface {
    //	private static String TAG = "UdpAnalyzerImpl";
    private static UdpAnalyzerImpl analyzerImpl;

    public static UdpAnalyzerImpl getInstans() {
        if (analyzerImpl == null) {
            analyzerImpl = new UdpAnalyzerImpl();
        }
        return analyzerImpl;
    }

    private JsonAssistant jsonAssistant = new JsonAssistant();

    @Override
    public void analy(byte[] buffer) {
        String data = new String(buffer).trim();
        if (data.contains("palpitation")) {
            //{"cmd":"palpitation","ip":"172.27.35.3"}#
            Palpitation palpitation = jsonAssistant.pasePalpitation(data);
            if (palpitation.getIp().equals(IpUtil.getLocalIpAddress(App.getAppContext()))) {
                return;//屏蔽自己发过来的心跳包
            }
            //读到心跳包刷新时间
            MouseService.palpitationTime = System.currentTimeMillis();
            Intent intent = new Intent();
            intent.setAction(MouseBusinessServiceImpl.DAO_UDP_UDPSERVER);
            intent.putExtra(MouseBusinessServiceImpl.CMD, palpitation.getCmd());
            intent.putExtra("palpitation", palpitation);
            // 发送自定义无序广播
            App.getAppContext().sendBroadcast(intent);
        }
    }

    /**
     * 重载一个UDP特殊的方法
     */
    public void analay(byte[] buffer, String hostAddress) {
        String data = new String(buffer).trim();
//		Log.i(TAG, "client收到："+data);
        if (data.startsWith("wlinkwulian")) {
//			if (isdispose) {
            // 发广播
            Intent intent = new Intent();
            intent.setAction(MouseBusinessServiceImpl.DAO_UDP_UDPSERVER);
            intent.putExtra(MouseBusinessServiceImpl.CMD, "wlinkwulian");
            // 发送自定义无序广播
            App.getAppContext().sendBroadcast(intent);
//				isdispose = false;
            saveEquipment(hostAddress);
//			}
        } else {
            this.analy(buffer);
        }
    }


    /*
     * 添加在线设备到list集合中
     */
    public void saveEquipment(String ip) {
        boolean isexist = false;
        List<Equipment> equipments = ChooseRoomActivity.equipments;
        for (Equipment equipment : equipments) {
            if (ip.equals(equipment.getIp())) {
                isexist = true;
                break;
            }
        }
        //不存在
        if (!isexist) {
            Equipment equipment = new Equipment();
            equipment.setIp(ip);
            ChooseRoomActivity.equipments.add(equipment);
            MouseService.equipment = equipment;
        }
    }
}
