
package server.yf.com.remotecontrolserver_as.minaserver;

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

    private IoSession session;
    private ConnectFuture future;
    private SocketConnector connector;

    public boolean start() {
        return start("139.196.98.226",9000);
    }

    public boolean start(String serverip, int port) {
        connector = new NioSocketConnector();
        connector.setConnectTimeoutMillis(30000);
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MessageProtocolCodecFactory(false)));
        connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 30);
        connector.setHandler(new IoServerHandler(this));

        future = connector.connect(new InetSocketAddress(serverip, port));
        future.awaitUninterruptibly();// 等待连接创建完成
        try {
            session = future.getSession();// 获得session
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 判断是否连接服务器成功
        if (session != null && session.isConnected()) {
            Log.d("MinaSocketConnector", "连接服务成功");
            return true;
        } else {
            Log.d("MinaSocketConnector", "连接服务器失败");
        }
        return false;
    }

    public void setAttribute(Object key, Object value) {
        session.setAttribute(key, value);
    }

    public void send(Object message) {
        session.write(message);
    }

    public void close() {
        CloseFuture future = session.getCloseFuture();
        future.awaitUninterruptibly(1000);
        connector.dispose();
    }

    public SocketConnector getConnector() {
        return connector;
    }

    public IoSession getSession() {
        return session;
    }

}
