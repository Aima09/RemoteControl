package server.yf.com.remotecontrolserver_as.LanMina;


import server.yf.com.remotecontrolserver_as.LanMina.library.encoderdecoder.MessageCodecFactory;

import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;

import server.yf.com.remotecontrolserver_as.ui.serice.MouseService;

/**
 * Created by sujuntao on 2017/7/1.
 */

public class MinaSocketConnector {
    private IoSession session;
    private ConnectFuture future;
    private SocketConnector connector;
    private NioSocketAcceptor acceptor;
    private InetSocketAddress socketAddress;

    public boolean start() {
        System.out.println("start");
        if (null == acceptor) {
            acceptor = new NioSocketAcceptor();
            //自己地址
            socketAddress = new InetSocketAddress(MouseService.equipment.getIp(), 18888);
            acceptor.getSessionConfig().setReadBufferSize(2048);
            acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 60);
//        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(
//                new TextLineCodecFactory(Charset.forName("UTF-8"),
//                        LineDelimiter.WINDOWS.getValue(),
//                        LineDelimiter.WINDOWS.getValue())));
            //acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MessageProtocolCodecFactory(true)));
            acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MessageCodecFactory()));
            acceptor.setHandler(new IoServerHandler(this));
            acceptor.setReuseAddress(true);
        }
        try {
            acceptor.bind(socketAddress);
            System.out.println("启动服务成功");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("启动服务失败");
            return false;
        }
    }

    public void setAttribute(Object key, Object value) {
        session.setAttribute(key, value);
    }

    public void send(Object message) {
        if (isConnect()){
            session.write(message);
        } else {
            connector.connect();
        }
    }

    public boolean isConnect(){
        return session.isConnected();
    }

    public void close() {
        try {
            CloseFuture future = session.getCloseFuture();
            future.awaitUninterruptibly(1000);
            connector.dispose();
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
    public void  setSession(IoSession ioSession){
        this.session=ioSession;
    }
}
