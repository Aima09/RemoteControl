
package com.yf.remotecontrolclient.minaclient.tcp;

import android.util.Log;

import com.yf.minalibrary.encoderdecoder.MessageProtocolCodecFactory;
import com.yf.remotecontrolclient.CommonConstant;
import com.yf.remotecontrolclient.activity.service.MouseService;

import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;

public class MinaSocketConnector {

    public static final String SERVER_DEFAULT_IP = "139.196.98.226";
    public static final int REMOTE_SERVER_DEFAULT_PROT = 9000;
    public static final int LOCAL_SERVER_DEFAULT_PROT = 18888;

    private IoSession session;
    private NioSocketConnector connector;
    private InetSocketAddress socketAddress;

    public boolean connectServer() {
        if (CommonConstant.LINE_TYPE == CommonConstant.LINE_TYPE_LOCAL) {
            if (null != socketAddress) {
                if (socketAddress.getPort() != LOCAL_SERVER_DEFAULT_PROT) {
                    close();
                } else {
                    if (!socketAddress.getHostName().equals(MouseService.equipment.getIp())) {
                        close();
                    }
                }
            }
            return connectServer(MouseService.equipment.getIp(), LOCAL_SERVER_DEFAULT_PROT);
        } else if (CommonConstant.LINE_TYPE == CommonConstant.LINE_TYPE_REMOTE) {
            if (null != socketAddress && socketAddress.getPort() != REMOTE_SERVER_DEFAULT_PROT) {
                close();
            }
            return connectServer(SERVER_DEFAULT_IP, REMOTE_SERVER_DEFAULT_PROT);
        } else {
            return false;
        }
    }

    private boolean connectServer(String serverIp, int port) {
        try {
            if (null == connector) {
                socketAddress = new InetSocketAddress(serverIp, port);
                connector = new NioSocketConnector();
                connector.setConnectTimeoutMillis(30000);
                connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MessageProtocolCodecFactory()));
                connector.getSessionConfig().setBothIdleTime(60);
                connector.setHandler(new IoClientHandler());
                connector.getSessionConfig().setKeepAlive(true);
//            connector.getSessionConfig().setReadBufferSize(2048 *10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 判断是否已连接服务器
        if (isConnect()) {
            Log.d("MinaSocketConnector", "已连接远程服务器");
            Log.d("MinaSocketConnector", "session.getServiceAddress():" + session.getServiceAddress());
            return true;
        } else {
            Log.d("MinaSocketConnector", "未连接远程服务器");
            return initSession();
        }
    }

    public boolean isConnect() {
        if (session != null && session.isConnected() && session.isActive()) {
            return true;
        } else {
            session = null;
            return false;
        }
    }

    private boolean initSession() {
        try {
            ConnectFuture future = connector.connect(socketAddress);
            future.awaitUninterruptibly();// 等待连接创建完成
            session = future.getSession();// 获得session
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isConnect()) {
            Log.d("MinaSocketConnector", "连接远程服务器成功");
            return true;
        } else {
            Log.d("MinaSocketConnector", "连接远程服务器失败");
            return false;
        }
    }

    public void send(Object message) {
        if (isConnect()) {
            if (null != message) {
                session.write(message);
            }
        }
    }

    public void close() {
        try {
            if (null != session) {
                CloseFuture future = session.getCloseFuture();
                future.awaitUninterruptibly(1000);
                session = null;
            }
            if (null != connector) {
                connector.dispose();
                connector = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
