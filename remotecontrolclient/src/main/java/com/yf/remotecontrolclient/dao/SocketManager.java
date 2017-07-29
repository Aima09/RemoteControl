package com.yf.remotecontrolclient.dao;


import android.content.Intent;
import android.text.TextUtils;

import com.yf.remotecontrolclient.App;
import com.yf.remotecontrolclient.CommonConstant;
import com.yf.remotecontrolclient.activity.ChooseRoomActivity;
import com.yf.remotecontrolclient.activity.service.MouseService;
import com.yf.remotecontrolclient.dao.tcpip.TCPIPServer;
import com.yf.remotecontrolclient.dao.udp.UDPServer;
import com.yf.remotecontrolclient.domain.Equipment;
import com.yf.remotecontrolclient.domain.Palpitation;
import com.yf.remotecontrolclient.service.imp.MouseBusinessServiceImpl;
import com.yf.remotecontrolclient.util.EquipmentFactory;
import com.yf.remotecontrolclient.util.GatewayFactory;
import com.yf.remotecontrolclient.util.IpUtil;
import com.yf.remotecontrolclient.util.SpUtil;

public class SocketManager {
    //	public static final String TAG = "SocketManager";
    private static SocketManager socketManager;

    public static SocketManager getSocketManager() {
        if (socketManager == null) {
            socketManager = new SocketManager();
        }
        return socketManager;
    }

    public void startUdp() {
        UDPServer.getInstans(MouseService.gateway, MouseService.equipment,
                UdpAnalyzerImpl.getInstans()).start();
        TCPIPServer.getInstans(MouseService.gateway, MouseService.equipment,
                TcpAnalyzerImpl.getInstans());
    }

    public void startTcp() {
        TCPIPServer.getInstans(MouseService.gateway, MouseService.equipment,
                TcpAnalyzerImpl.getInstans()).start();
    }

    boolean ischeck = true;

    // 开启心跳
    public void check() {
//		Log.i(TAG, "check");
        if (ischeck) {
            // 开始发心跳包
            new Thread() {
                public void run() {
                    ischeck = false;
                    while (true) {
                        try {
                            long time1 = System.currentTimeMillis();
                            Palpitation palpitation = new Palpitation();
                            palpitation.setCmd("palpitation");
                            palpitation.setIp(IpUtil.getLocalIpAddress(App
                                    .getAppContext()));
                            new MouseBusinessServiceImpl()
                                    .sendPalpitation(palpitation);
//							Log.i(TAG,"心跳包1:"+ (System.currentTimeMillis()
//									- MouseService.palpitationTime )+"秒");
                            Thread.sleep(Math.max(2 * 1000 - (System
                                    .currentTimeMillis() - time1), 0));
                            long time2 = System.currentTimeMillis();
                            if (System.currentTimeMillis()
                                    - MouseService.palpitationTime > 6 * 1000
                                    && MouseService.palpitationTime != 0) {
                                // 服务端已经挂了
//								Log.i(TAG, "重启");
                                restart();
                                ischeck = true;
                                //刷新界面
//								QRCodeScanActivity activity=QRCodeScanActivity.getMainActivity();
//								Message message=activity.handler.obtainMessage();
//								message.obj="正在寻找。。";
//								message.what=1;
//								activity.handler.sendMessage(message);
                                SpUtil.putString(App.getAppContext(), CommonConstant.CONNECTION_KEY, "正在寻找。。");
                                Intent intent = new Intent();
                                intent.setAction(CommonConstant.BASEACTIVITY_MBASEACTIVITYBROADCASTRECEIVER);
                                intent.putExtra("data", "正在寻找");
                                App.getAppContext().sendBroadcast(intent);
                                return;
                            }
                            Palpitation palpitation1 = new Palpitation();
                            palpitation1.setCmd("palpitation");
                            palpitation1.setIp(IpUtil.getLocalIpAddress(App
                                    .getAppContext()));
                            new MouseBusinessServiceImpl()
                                    .sendPalpitation(palpitation1);
//							Log.i(TAG,"心跳包2:"+ (System.currentTimeMillis()
//									- MouseService.palpitationTime )+"秒");
                            Thread.sleep(Math.max(2 * 1000 - (System
                                    .currentTimeMillis() - time2), 0));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                ;
            }.start();
        }
    }

    public static boolean iscolle = true;

    public void outoCollection() {
        new Thread() {
            @Override
            public void run() {
                while (iscolle) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
//					Log.i(TAG, "连接");
                    if (ChooseRoomActivity.equipments != null
                            && ChooseRoomActivity.equipments.size() > 0) {
                        // 有设备在线
//						Log.i(TAG, "有设备在线");
//						Message message2 = QRCodeScanActivity.handler.obtainMessage();
//						message2.obj = "有设备在线";
//						message2.what = 1;
//						QRCodeScanActivity.handler.sendMessage(message2);
                        SpUtil.putString(App.getAppContext(), CommonConstant.CONNECTION_KEY, "有设备在线");
                        Intent intent = new Intent();
                        intent.setAction(CommonConstant.BASEACTIVITY_MBASEACTIVITYBROADCASTRECEIVER);
                        intent.putExtra("data", "有设备在线");
                        App.getAppContext().sendBroadcast(intent);
                        String ip = SpUtil.getString(App.getAppContext(),
                                CommonConstant.IP_KEY, null);
                        if (TextUtils.isEmpty(ip)) {// 说明第一次打开本软件
                            SpUtil.putString(App.getAppContext(), CommonConstant.IP_KEY,
                                    ChooseRoomActivity.equipments.get(0)
                                            .getIp());
//							Log.i(TAG, "ifput");
                        } else {
//							SocketManager.getSocketManager().restart();
//							Log.i(TAG, "elseget");
                            for (Equipment equipment : ChooseRoomActivity.equipments) {
                                if (ip.equals(equipment.getIp())) {
                                    MouseService.equipment = equipment;
                                }
                            }
                            // 发起连接
                            // 开启tcp
                            SocketManager.getSocketManager().startTcp();

                            // 放回数据
                            // Log.i(TAG, "广播接收成功2");
                            if (MouseService.equipment.getIp() != null) {
                                new MouseBusinessServiceImpl().echoEquipment(MouseService.gateway);
//								Message message3 = QRCodeScanActivity.handler.obtainMessage();
//								message3.obj = "正在连接。。";
//								message3.what = 1;
//								QRCodeScanActivity.handler.sendMessage(message3);
                                SpUtil.putString(App.getAppContext(), CommonConstant.CONNECTION_KEY, "有设备在线");
                                Intent intent1 = new Intent();
                                intent1.setAction(CommonConstant.BASEACTIVITY_MBASEACTIVITYBROADCASTRECEIVER);
                                intent1.putExtra("data", "正在连接。。");
                                App.getAppContext().sendBroadcast(intent1);
                                return;
                            } else {
                                for (int i = 0; i < ChooseRoomActivity.equipments.size(); i++) {
                                    if (!ChooseRoomActivity.equipments.get(i).getIp().equals(MouseService.equipment.getIp())) {
                                        ChooseRoomActivity.equipments.remove(i);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }.start();
    }

    public void stop() {
        UDPServer.getInstans(MouseService.gateway, MouseService.equipment,
                UdpAnalyzerImpl.getInstans()).stop();
        TCPIPServer.getInstans(MouseService.gateway, MouseService.equipment,
                TcpAnalyzerImpl.getInstans()).stop();
        MouseService.gateway = GatewayFactory.getGateway();
        MouseService.equipment = EquipmentFactory.getEquipment();
    }

    public void restart() {
        // 发起连接请求
        MouseService.palpitationTime = 0;
        //清空、
        ChooseRoomActivity.equipments.clear();
//		TcpAnalyzerImpl.isdispose=true;
        getSocketManager().stop();
        getSocketManager().startUdp();
        iscolle = true;
        outoCollection();
    }
}
