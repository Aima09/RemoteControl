
package server.yf.com.remotecontrolserver_as.minamachines;

import android.util.Log;

import com.yf.minalibrary.encoderdecoder.MessageProtocolCodecFactory;

import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;

public class MinaSocketConnector {

    public static final String SERVER_DEFAULT_IP = "139.196.98.226";
    public static final int SERVER_DEFAULT_PROT = 9000;

    private IoSession session;
    private ConnectFuture future;
    private NioSocketConnector connector;
    private InetSocketAddress socketAddress;

    public boolean connectServer() {
//        return connectServer(SERVER_DEFAULT_IP,SERVER_DEFAULT_PROT);
        return connectServer(SERVER_DEFAULT_IP,SERVER_DEFAULT_PROT);
    }

    public boolean connectServer(String serverIp, int port) {
        if (null == connector){
            socketAddress = new InetSocketAddress(serverIp, port);
            connector = new NioSocketConnector();
            connector.setConnectTimeoutMillis(30000);
            connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MessageProtocolCodecFactory(false)));
            connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 30);
            connector.setHandler(new IoClientHandler(this));
        }
        // 判断是否已连接服务器
        if (isConnect()) {
            Log.d("MinaSocketConnector", "已连接服务器");
            return true;
        } else {
            Log.d("MinaSocketConnector", "未连接服务器");
            return reConnectServer();
        }
    }

    public boolean isConnect(){
        return session != null && session.isConnected();
    }

    public boolean reConnectServer(){
        try {
            future = connector.connect(socketAddress);
            future.awaitUninterruptibly();// 等待连接创建完成
            session = future.getSession();// 获得session
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isConnect()){
            Log.d("MinaSocketConnector", "连接服务器成功");
            return true;
        } else {
            Log.d("MinaSocketConnector", "连接服务器失败");
            return false;
        }
    }

    public void setAttribute(Object key, Object value) {
        if (null != session){
            session.setAttribute(key, value);
        }
    }

    public void send(Object message) {
        if (isConnect()){
            if (null != message){
                session.write(message);
            }
        } else {
            connectServer();
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

    public SocketConnector getConnector() {
        return connector;
    }

    public IoSession getSession() {
        return session;
    }

}
