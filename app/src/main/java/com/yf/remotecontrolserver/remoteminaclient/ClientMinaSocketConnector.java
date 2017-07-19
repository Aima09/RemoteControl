
package com.yf.remotecontrolserver.remoteminaclient;

import android.util.Log;

import com.yf.minalibrary.encoderdecoder.MessageProtocolCodecFactory;

import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;

public class ClientMinaSocketConnector {

    public static final String SERVER_DEFAULT_IP = "139.196.98.226";
//    public static final String SERVER_DEFAULT_IP = "192.168.1.55";
    public static final int SERVER_DEFAULT_PROT = 9000;

    private IoSession session;
    private ConnectFuture future;
    private NioSocketConnector connector;
    private InetSocketAddress socketAddress;

    public boolean connectServer() {
        return connectServer(SERVER_DEFAULT_IP,SERVER_DEFAULT_PROT);
    }

    public boolean connectServer(String serverIp, int port) {
        if (null == connector){
            socketAddress = new InetSocketAddress(serverIp, port);
            connector = new NioSocketConnector();
            connector.setConnectTimeoutMillis(30000);
            connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MessageProtocolCodecFactory(false)));
            connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 60);
//            connector.getSessionConfig().setReadBufferSize(2048 * 10);
//            connector.getSessionConfig().setReceiveBufferSize(2048 * 10);
            connector.getSessionConfig().setKeepAlive(true);
            connector.setHandler(new IoClientHandler());
        }
        // 判断是否已连接服务器
            Log.d("ClientMinaSocketConnect", "connector.getConnectTimeoutCheckInterval():" + connector.getConnectTimeoutCheckInterval());
        if (isConnect()) {
            Log.d("ClientMinaSocketConnect", "已连接远程服务器");
            Log.d("ClientMinaSocketConnect", "session.getServiceAddress():" + session.getServiceAddress());
            return true;
        } else {
            Log.d("ClientMinaSocketConnect", "未连接远程服务器");
            return reConnectServer();
        }
    }

    public boolean isConnect(){
        if (null != session && session.isConnected() && session.isActive()){
            return true;
        } else {
            session = null;
            return false;
        }
    }

    private boolean reConnectServer(){
        try {
            future = connector.connect(socketAddress);
            future.awaitUninterruptibly();// 等待连接创建完成
            session = future.getSession();// 获得session
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 判断是否已连接服务器
        if (isConnect()) {
            Log.d("ClientMinaSocketConnect", "连接远程服务器成功");
            Log.d("ClientMinaSocketConnect", "session.getServiceAddress():" + session.getServiceAddress());
            return true;
        } else {
            Log.d("ClientMinaSocketConnect", "连接远程服务器失败");
            return false;
        }
    }

    public void send(Object message) {
        if (isConnect()){
            if (null != message){
                session.write(message);
            }
        }
    }

    public void close() {
        try {
            CloseFuture future = session.getCloseFuture();
            future.awaitUninterruptibly(1000);
            connector.dispose();
            connector = null;
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
