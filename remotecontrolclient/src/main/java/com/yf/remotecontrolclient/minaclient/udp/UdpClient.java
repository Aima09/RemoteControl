package com.yf.remotecontrolclient.minaclient.udp;

import android.util.Log;

import java.net.InetSocketAddress;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;

public class UdpClient {
    private IoConnector connector;
    private static IoSession session;
    public int PORT = 6000;

    public boolean startUpdClinet() {
        connector = new NioDatagramConnector();
        connector.setHandler(new UdpClientHander());
        DatagramSessionConfig dcfg = (DatagramSessionConfig) connector.getSessionConfig();
        ConnectFuture connFuture = connector.connect(new InetSocketAddress("192.168.1.102", PORT));
        connFuture.awaitUninterruptibly();// 等待连接创建完成
        try {
            session = connFuture.getSession();// 获得session
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 判断是否连接服务器成功
        if (session != null && session.isConnected()) {
            Log.d("UdpClient", "连接服务成功");
            return true;
        } else {
            Log.d("UdpClient", "连接服务器失败");
        }
        return false;
    }

    public void send(Object message) {
        session.write(message);
    }

}
