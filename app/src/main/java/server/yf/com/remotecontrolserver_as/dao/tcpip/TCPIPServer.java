package server.yf.com.remotecontrolserver_as.dao.tcpip;


import android.content.Intent;
import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import server.yf.com.remotecontrolserver_as.App;
import server.yf.com.remotecontrolserver_as.dao.AnalyzerInterface;
import server.yf.com.remotecontrolserver_as.domain.Boot;
import server.yf.com.remotecontrolserver_as.domain.Equipment;
import server.yf.com.remotecontrolserver_as.domain.Gateway;
import server.yf.com.remotecontrolserver_as.service.impl.MouseBusinessServiceImpl;
import server.yf.com.remotecontrolserver_as.ui.serice.MouseService;

public class TCPIPServer {
//	private static String TAG = "TCPIPServer";
	
	private static TCPIPServer tcpipServer;
	public static TCPIPServer getInstans(Gateway gateway, Equipment equipment,
										 AnalyzerInterface analyzerInterface){
		if(tcpipServer==null){
			tcpipServer=new TCPIPServer(gateway,equipment,analyzerInterface);
		}
	    return tcpipServer;
	} 
	
	private AnalyzerInterface analyzerInterface;
	private Gateway gateway;
	private Equipment equipment;
	private Boolean isStopRead = false;
	// 开启TCP线程
	private Thread startThread;
	private Socket socket;
	private Thread readThread;
	private InputStream inputStream;
	byte[] rb;
	private Thread writThread;
	private OutputStream outputStream;
	private byte[] wb;

	public TCPIPServer(Gateway gateway, Equipment equipment,
			AnalyzerInterface analyzerInterface) {
		super();
		this.analyzerInterface = analyzerInterface;
		this.equipment = equipment;
		this.gateway = gateway;
	}

	// 写方法
	public void send(byte[] data) {
		wb = data;
//		Log.i(TAG, "写出"+new String(data).trim());
		// 每写一次启动写线程
		if (writThread == null) {
			writThread = new Thread(new writeRunnable());
			writThread.start();
		} else {
			if (writThread.isAlive()) {// 测试线程是否处于活动状态，相当于 run 是否还在执行。
//				Log.e(TAG, "写线程还在执行，丢失写的数据" + new String(wb).trim());
				return;
			}
		}
	}

	// 启动
	public void start() {
		// 读boolean归位
		isStopRead = false;
		startThread = new Thread(new startRunnable());
		startThread.start();
	}

	public void stop() {
		isStopRead = true;
		startThread = null;
		writThread = null;
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			inputStream = null;
		}
		if (outputStream != null) {
			try {
				outputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			outputStream = null;
		}
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			socket = null;
		}
	}

	class writeRunnable implements Runnable {
		@Override
		public void run() {
			try {
				if (socket != null && outputStream != null) {
//					Log.i(s, "写出"+new String(wb).trim());
					outputStream.write(wb);
				} else {
//					Log.e(TAG, "写数据时socket、serverSocket、inputStream为空");
				}
			} catch (Exception e) {
				throw new RuntimeException();
			}
			wb = null;
			writThread = null;
		}
	}

	String readString;
	StringBuilder builder=new StringBuilder();
	class readRunnable implements Runnable {
		@Override
		public void run() {
			try {
				while (!isStopRead) {
					rb = null;
//					Log.i(TAG, "读数据之前");
					if (inputStream != null) {
						rb = new byte[1024];
						inputStream.read(rb);
						readString = new String(rb).trim();
						if (!TextUtils.isEmpty(readString)) {
//								// 读到数据
								String[] sp = new String(rb).trim().split("#");
								for (int i = 0; i < sp.length; i++) {
									if (!TextUtils.isEmpty(sp[i])) {
//										Log.i(TAG, "服务端读数据"+new String(sp[i]));
										analyzerInterface.analy(sp[i].getBytes(),null);
									}
								}
//							if(readString.contains("#")){
//								int index=readString.indexOf("#");
//								builder.append(readString.substring(0, index));
//								analyzerInterface.analy(builder.toString().getBytes());
//								builder=new StringBuilder();
//								builder.append(readString.substring(index+1));
//							}else{
//								builder.append(readString);
//							}
						}
//						Log.i(TAG, "读不为空");
					}
//					Log.i(TAG, "读数据之后");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		
	}
	class startRunnable implements Runnable {
		@Override
		public void run() {
			try {
				socket = new Socket(MouseService.gateway.getGwIp(),
						MouseService.gateway.getGwTcpPort());
				inputStream = socket.getInputStream();
				outputStream = socket.getOutputStream();
				// 开启读
				readThread = new Thread(new readRunnable());
				readThread.start();
				// 创建连接好的话发广播
				Boot boot = new Boot();// 创建boot
				boot.setCmd("BSboot");
				boot.setDevid(MouseService.equipment.getDevid());
				Intent intent = new Intent();
				intent.setAction(MouseBusinessServiceImpl.DAO_UDP_UDPSERVER);
				intent.putExtra(MouseBusinessServiceImpl.CMD, "BSboot");
				intent.putExtra("boot", boot);
				App.getAppContext().sendBroadcast(intent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
