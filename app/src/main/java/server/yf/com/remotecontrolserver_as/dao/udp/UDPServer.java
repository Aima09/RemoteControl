package server.yf.com.remotecontrolserver_as.dao.udp;


import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import server.yf.com.remotecontrolserver_as.App;
import server.yf.com.remotecontrolserver_as.dao.AnalyzerInterface;
import server.yf.com.remotecontrolserver_as.domain.Equipment;
import server.yf.com.remotecontrolserver_as.domain.Gateway;
import server.yf.com.remotecontrolserver_as.ui.serice.MouseService;

public class UDPServer {
//	private static String TAG = "UDPServer";
	private static UDPServer udpServer;
	public static UDPServer getInstans(Gateway gateway, Equipment equipment, AnalyzerInterface analyzerInterface){
		if(udpServer==null){
			udpServer=new UDPServer(gateway,equipment,analyzerInterface);
		}
	    return udpServer;
	}
	
	private AnalyzerInterface analyzerInterface;
	private Gateway gateway;
	private Equipment equipment;
	public boolean isStopRead=false;
	private DatagramSocket datagramSocket;
	//开启udp线程
	private Thread startThread;
	private Thread readThread;
	private Thread writThread;
	private byte[] wb; 
	private boolean isStopWrite=false;
	public UDPServer(Gateway gateway,Equipment equipment, AnalyzerInterface analyzerInterface) {
		this.gateway = gateway;
		this.equipment=equipment;
		this.analyzerInterface = analyzerInterface;
	}

	//写方法
	public void send(byte[] data){
		wb=data;
//		Log.e(TAG, "发送前"+new String(wb).trim());
		//每写一次启动写线程
		if (writThread == null) {
			writThread=new Thread(new WriteRunnable());
			writThread.start();
        } else { 
            if (writThread.isAlive()) {//测试线程是否处于活动状态，相当于 run 是否还在执行。
//               Log.e(TAG, "写线程还在执行，丢失写的数据"+new String(wb).trim());
               return;
            }
        }
	}
	
	// 启动
	public void start() {
		try {
			ip=getBroadcastAddress(App.getAppContext());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		isStopRead=false;
		isStopWrite=false;
		startThread=new Thread(new StartRunnable());
		startThread.start();
		readThread=new Thread(new ReadRunnable());
		readThread.start();
		
	}
	public void startConnection(){
		isStopWrite=false;
		try {
			ip=getBroadcastAddress(App.getAppContext());
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// 开启就要不停的写
		new Thread(){
			@Override
			public void run() {
				try {
					while (!isStopWrite) {
						send("wlinkwulian123456".getBytes());
						Thread.sleep(1000);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	public void stopConnection(){
		// 结束不停的写
		isStopWrite=true;
		try {
			ip= InetAddress.getByName(MouseService.gateway.getGwIp());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void stop(){
		new Thread(){
			public void run() {
				isStopRead=true;
				isStopWrite=true;
				readThread=null;
				writThread=null;
				if(datagramSocket!=null){
					datagramSocket.close();
					datagramSocket=null;
				}
			};
		}.start();
	}
	InetAddress ip;
	class WriteRunnable implements Runnable {
		

		@Override
		public void run() { 
			if(datagramSocket!=null){
				try{
					DatagramPacket writedatagramPacket = new DatagramPacket(wb,wb.length ,ip, 7320);
					datagramSocket.setBroadcast(true);
					datagramSocket.send(writedatagramPacket);
				}catch(Exception e){
					e.printStackTrace();
				}
//				Log.i(TAG, "发出的数据"+new String(wb));
				writThread=null;
			}else{
//				Log.e(TAG, "写数据时datagramSocket为空");
			}
		}
	}
	
	public static InetAddress getBroadcastAddress(Context context) throws UnknownHostException {
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		DhcpInfo dhcp = wifi.getDhcpInfo();
		if (dhcp == null) {
			return InetAddress.getByName("255.255.255.255");
		}
		int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
		byte[] quads = new byte[4];
		for (int k = 0; k < 4; k++)
			quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
		String a=InetAddress.getByAddress(quads).getHostAddress();
		return InetAddress.getByAddress(quads);
	}
	class ReadRunnable implements Runnable {
		@Override
		public void run() {
			try{
				while (!isStopRead) {
					if(datagramSocket!=null){
						byte[] rb=new byte[1024];
						DatagramPacket readdatagramPacket=new DatagramPacket(rb, rb.length);
						datagramSocket.receive(readdatagramPacket);
						if(rb.length>0){
//							Log.i(TAG, "读到的数据"+new String(rb).trim());
//							Log.i(TAG, "读到的数据ip"+readdatagramPacket.getAddress().getHostAddress());
							MouseService.gateway.setGwIp(readdatagramPacket.getAddress().getHostAddress());
							analyzerInterface.analy(rb);
						}
					}
				}
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	class StartRunnable implements Runnable {
		@Override
		public void run() {
			try {
				datagramSocket = new DatagramSocket(7320);
			} catch (Exception e) {
				e.printStackTrace();
//				Log.e(TAG, "创建datagramSocket异常");
			}
		} 
	}
}
