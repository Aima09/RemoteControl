package server.yf.com.remotecontrolserver_as.ui.serice;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import server.yf.com.remotecontrolserver_as.dao.SocketManager;
import server.yf.com.remotecontrolserver_as.domain.Action;
import server.yf.com.remotecontrolserver_as.domain.Boot;
import server.yf.com.remotecontrolserver_as.domain.Equipment;
import server.yf.com.remotecontrolserver_as.domain.Gateway;
import server.yf.com.remotecontrolserver_as.domain.Writer;
import server.yf.com.remotecontrolserver_as.service.MouseBusinessService;
import server.yf.com.remotecontrolserver_as.service.impl.MouseBusinessServiceImpl;
import server.yf.com.remotecontrolserver_as.util.EquipmentFactory;
import server.yf.com.remotecontrolserver_as.util.GatewayFactory;

public class MouseService extends Service {
	public static final String TAG = "MouseService";
//	// 业务
	private static MouseBusinessService mouseBusinessService;
//	// 解析器
//	private MouseAnalyzerInterface analyzerInterface;
//	// udp负责类
//	private static UDPServer udpServer;
//	// tcp负责类
//	private  TCPIPServer tcpipServer;
	public static long palpitationTime;
	public static Gateway gateway = GatewayFactory.getGateway();
	public static Equipment equipment = EquipmentFactory.getEquipment();
	
	// 广播接收者 （接收信息）
	private BroadcastReceiver mybroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// Log.i(TAG, "广播接收成功");
			String action = intent.getAction();
			// Log.i(TAG, "action=" + action);
			// tcp发过来的消息
			if (action.equals(MouseBusinessServiceImpl.DAO_TCPIP_TCPIPSERVER)) {
				String cmd = intent.getStringExtra(MouseBusinessServiceImpl.CMD);
//				Log.i(TAG, cmd);
				if (cmd.equals("BSboot")) {
					// BSboot 收到boot应该显示连接成功
					Boot boot = (Boot) intent.getSerializableExtra("boot");
					//心跳检测
					SocketManager.getSocketManager().check();
//					Log.i(TAG, "收到boot");
					// Log.i("boot", boot.toString());
					SocketManager.getSocketManager().stopConnection();
				} else if (cmd.equals("action")) {
					Action act = (Action) intent.getSerializableExtra("action");
//					Log.i(TAG, "action"+act.getCmd()+"数据"+act.getData());
					if ("move".equals(act.getCmd())) {
						mouseBusinessService.move(act.getData());
					} else if ("mode".equals(act.getCmd())) {
						mouseBusinessService.mode(act.getData());
					} else if ("KEY".equals(act.getCmd())) {
						mouseBusinessService.key(act.getData());
					} else if ("home".equals(act.getCmd())) {
						mouseBusinessService.home();
					}
				} else if (cmd.equals("startWrite")) {
					// 开启键盘界面
//					Log.i(TAG, "startWrite");
				} else if ("write".equals(cmd)) {
					Writer writer = (Writer) intent
							.getSerializableExtra("writer");
					//发广播
					Intent intent2 = new Intent();
					intent2.setAction("android.inputmethodservice.MyInputMethodService");
					intent2.putExtra("data", writer.getData());
					getApplicationContext().sendBroadcast(intent2);
				} else if ("exitnetwork".equals(cmd)) {
					// 客户端提示退出了
					mouseBusinessService.exit();
				}
				// udp发过来的消息
			} else if (action.equals(MouseBusinessServiceImpl.DAO_UDP_UDPSERVER)) {
				 String cmd = intent.getStringExtra(MouseBusinessServiceImpl.CMD);
//				 Log.i(TAG, "udp发过来的消息"+cmd);
				//放回数据
				if (cmd.equals("gateway")) {
					Gateway gateway1 = (Gateway) intent
							.getSerializableExtra("gateway");
					MouseService.gateway.setGwID(gateway1.getGwID());
					MouseService.gateway.setGwIp(gateway1.getGwIp());
					MouseService.gateway.setGwPort(gateway1.getGwPort());
//					Log.i(TAG, "MouseService.gateway.toString():"+MouseService.gateway.toString());
					// 启动tcp
					SocketManager.getSocketManager().startTcp();
				}else if ("palpitation".equals(cmd)) {
					// 收到心跳
//					Log.i(TAG, "收到心跳");
					palpitationTime=System.currentTimeMillis();
				} else if(cmd.equals("BSboot")){
					//SocketManager.getSocketManager().startTcp();开启成功
					//发boot
					Boot boot = (Boot) intent.getSerializableExtra("boot");
					mouseBusinessService.linkGateway(boot);
				}
			}
		}
	};
	
	@Override
	public void onCreate() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(MouseBusinessServiceImpl.DAO_UDP_UDPSERVER);
		filter.addAction(MouseBusinessServiceImpl.DAO_TCPIP_TCPIPSERVER);
		// 3.注册广播接收者
		registerReceiver(mybroadcastReceiver, filter);
		//开启
		SocketManager.getSocketManager().startUdp();
		SocketManager.getSocketManager().startConnection();
		//初始化
		mouseBusinessService = new MouseBusinessServiceImpl();
		// 发起连接
		mouseBusinessService.echoGateway(MouseService.equipment);
		super.onCreate();
	}
	
	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
