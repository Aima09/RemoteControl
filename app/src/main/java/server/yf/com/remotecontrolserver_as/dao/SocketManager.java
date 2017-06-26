package server.yf.com.remotecontrolserver_as.dao;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

import server.yf.com.remotecontrolserver_as.App;
import server.yf.com.remotecontrolserver_as.dao.tcpip.TCPIPServer;
import server.yf.com.remotecontrolserver_as.dao.udp.UDPServer;
import server.yf.com.remotecontrolserver_as.domain.Palpitation;
import server.yf.com.remotecontrolserver_as.service.impl.MouseBusinessServiceImpl;
import server.yf.com.remotecontrolserver_as.ui.serice.MouseService;
import server.yf.com.remotecontrolserver_as.util.EquipmentFactory;
import server.yf.com.remotecontrolserver_as.util.GatewayFactory;
import server.yf.com.remotecontrolserver_as.util.IpUtil;

public class SocketManager {
	public static final String TAG = "SocketManager";
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
	}

	public void startTcp() {
		//不需要关
//		UDPServer.getInstans(MouseService.gateway, MouseService.equipment,
//				UdpAnalyzerImpl.getInstans()).stop();
		TCPIPServer.getInstans(MouseService.gateway, MouseService.equipment,
				TcpAnalyzerImpl.getInstans()).start();
		stopConnection();
	}
	
	public void startConnection(){
		UDPServer.getInstans(MouseService.gateway, MouseService.equipment,
				UdpAnalyzerImpl.getInstans()).startConnection();
	}
	public void stopConnection(){
		UDPServer.getInstans(MouseService.gateway, MouseService.equipment,
				UdpAnalyzerImpl.getInstans()).stopConnection();
	}

	boolean ischeck = true;
	Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(ischeck==true){
				MouseService.palpitationTime=1;
				check();
			}
		}
	};
	
	// 开启心跳
	public void check() {
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
							long time2=System.currentTimeMillis();
//							Log.i(TAG, "检测时的时间"+MouseService.palpitationTime);
							if ((System.currentTimeMillis()
									- MouseService.palpitationTime) > 4 * 1000
									&& MouseService.palpitationTime != 0) {
								// 客户端已经挂了
								Log.i(TAG, "重启");
								restart();
								ischeck = true;
								handler.sendMessageDelayed(handler.obtainMessage(), 3000);
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
				};
			}.start();
		}
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
		getSocketManager().stop();
		getSocketManager().startUdp();
		getSocketManager().startConnection();
		new MouseBusinessServiceImpl().echoGateway(MouseService.equipment);
	}
}
