package com.yf.remotecontrolclient.activity.service;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;

import com.yf.remotecontrolclient.dao.AnalyzerInterface;
import com.yf.remotecontrolclient.dao.TcpAnalyzerImpl;
import com.yf.remotecontrolclient.dao.tcpip.TCPIPServer;
import com.yf.remotecontrolclient.dao.udp.UDPServer;
import com.yf.remotecontrolclient.domain.Boot;
import com.yf.remotecontrolclient.domain.Equipment;
import com.yf.remotecontrolclient.domain.Gateway;
import com.yf.remotecontrolclient.service.MouseBusinessService;
import com.yf.remotecontrolclient.service.imp.MouseBusinessServiceImpl;
import com.yf.remotecontrolclient.util.EquipmentFactory;
import com.yf.remotecontrolclient.util.GatewayFactory;

public class MouseService extends Service {
    //	public final String TAG="MouseService";
    //业务
    private MouseBusinessService mouseBusinessService;
    //解析器
    private AnalyzerInterface analyzerInterface;
    //udp负责类
    private UDPServer udpServer;
    //tcp负责类
    private TCPIPServer tcpipServer;
    MyBinder mbinder = new MyBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mbinder;
    }

    public static Gateway gateway = GatewayFactory.getGateway();
    public static Equipment equipment = EquipmentFactory.getEquipment();
    public static long palpitationTime = 0;
    //广播接收者 （接收信息）
    private BroadcastReceiver mybroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
//			Log.i(TAG, "广播接收成功");
            String action = intent.getAction();

//			if(action.equals(MouseBusinessServiceImpl.DAO_UDP_UDPSERVER)){
//				//放回数据
//				Log.i(TAG, "广播接收成功2");
//				mouseBusinessService.echoEquipment(gateway);
//				//启动tcp
//				tcpipServer.start();
//			}else if(action.equals(MouseBusinessServiceImpl.DAO_TCPIP_TCPIPSERVER)){
//				Boot boot=(Boot) intent.getSerializableExtra("boot");
//				//handler消息叫页面出现按钮，按钮的Text为eq的dvid
//				Message message=QRCodeScanActivity.handler.obtainMessage();
//				message.obj=boot;
//				message.what=0;
//				QRCodeScanActivity.handler.sendMessage(message);
//			}
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(MouseBusinessServiceImpl.DAO_UDP_UDPSERVER);
        //3.注册广播接收者  
        registerReceiver(mybroadcastReceiver, filter);
//		Log.i(TAG, "服务");
        analyzerInterface = new TcpAnalyzerImpl();
        udpServer = new UDPServer(gateway, equipment, analyzerInterface);
        udpServer.start();
//		Log.i(TAG, "udp成功启动");
//		tcpipServer=new TCPIPServer(gateway,equipment, analyzerInterface);
//		mouseBusinessService=new MouseBusinessServiceImpl(tcpipServer, udpServer);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mybroadcastReceiver);
        super.onDestroy();
    }

    public class MyBinder extends Binder {
        public void link(Boot boot) {
            //mouseBusinessService.linkEquipment(boot);
        }
    }
}
