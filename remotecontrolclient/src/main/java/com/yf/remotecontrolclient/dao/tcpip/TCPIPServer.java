package com.yf.remotecontrolclient.dao.tcpip;


import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.yf.remotecontrolclient.activity.service.MouseService;
import com.yf.remotecontrolclient.dao.AnalyzerInterface;
import com.yf.remotecontrolclient.domain.Equipment;
import com.yf.remotecontrolclient.domain.Gateway;

public class TCPIPServer {
//	private static String TAG = "TCPIPServer";

    private static TCPIPServer tcpipServer;

    public static TCPIPServer getInstans(Gateway gateway, Equipment equipment,
                                         AnalyzerInterface analyzerInterface) {
        if (tcpipServer == null) {
            tcpipServer = new TCPIPServer(gateway, equipment, analyzerInterface);
        }
        return tcpipServer;
    }

    private AnalyzerInterface analyzerInterface;
    private Gateway gateway;
    private Equipment equipment;
    private boolean isStopRead = false;
    // 开启TCP线程
    private Thread startThread;
    private ServerSocket serverSocket;
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
        // 每写一次启动写线程
        if (writThread == null) {
            writThread = new Thread(new writeRunnable());
            writThread.start();
        } else {
            if (writThread.isAlive()) {// 测试线程是否处于活动状态，相当于 run 是否还在执行。
//					Log.e(TAG, "写线程还在执行，丢失写的数据" + new String(wb).trim());
                return;
            }
        }
    }

    // 启动
    public void start() {
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
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            serverSocket = null;
        }
    }

    class writeRunnable implements Runnable {
        @Override
        public void run() {
            try {
                if (socket != null && serverSocket != null
                        && outputStream != null) {
//					Log.i(TAG, "客户端写数据"+new String(wb));
                    outputStream.write(wb);
                    outputStream.flush();
                } else {
//					Log.e(TAG, "写数据时socket、serverSocket、inputStream为空");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            wb = null;
            writThread = null;
        }
    }

    StringBuilder builder = new StringBuilder();

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
                        String readString = new String(rb).trim();
//						Log.i(TAG, "客户端解析前"+readString);
                        if (!TextUtils.isEmpty(readString)) {
//							// 读到数据
//							if(readString.contains("#")){
//								int index=readString.indexOf("#");
//								builder.append(readString.substring(0, index));
//								analyzerInterface.analy(builder.toString().getBytes());
//								Log.i(TAG, "客户端解析数据"+builder.toString());
//								builder=new StringBuilder();
//								builder.append(readString.substring(index+1));
//							}else{
//								builder.append(readString);
//							}
                            String[] sp = new String(rb).trim().split("#");
                            for (int i = 0; i < sp.length; i++) {
                                if (!TextUtils.isEmpty(sp[i])) {
//									Log.i(TAG, "读数据"+new String(sp[i]));
                                    analyzerInterface.analy(sp[i].getBytes());
                                }
                            }
                        }
//						
                    }
//					Thread.sleep(500);
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
                serverSocket = new ServerSocket(
                        MouseService.gateway.getGwTcpPort());// 服务端的端口
                socket = serverSocket.accept();
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
                //开启读
                readThread = new Thread(new readRunnable());
                readThread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
